/*
 * Copyright 2024 OmniOne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omnione.did.wallet.v1.agent.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.datamodel.data.AccEcdh;
import org.omnione.did.base.datamodel.data.Candidate;
import org.omnione.did.base.datamodel.data.DidAuth;
import org.omnione.did.base.datamodel.data.EcdhReqData;
import org.omnione.did.base.datamodel.enums.EccCurveType;
import org.omnione.did.base.datamodel.enums.ProofPurpose;
import org.omnione.did.base.datamodel.enums.SymmetricCipherType;
import org.omnione.did.base.db.domain.CertificateVc;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.property.WalletProviderProperty;
import org.omnione.did.base.utils.*;
import org.omnione.did.common.util.DateTimeUtil;
import org.omnione.did.common.util.JsonUtil;
import org.omnione.did.crypto.exception.CryptoException;
import org.omnione.did.crypto.keypair.EcKeyPair;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.enums.did.ProofType;
import org.omnione.did.data.model.vc.VerifiableCredential;
import org.omnione.did.wallet.v1.agent.api.dto.ConfirmEnrollEntityApiReqDto;
import org.omnione.did.wallet.v1.agent.api.dto.ConfirmEnrollEntityApiResDto;
import org.omnione.did.wallet.v1.agent.api.dto.ProposeEnrollEntityApiReqDto;
import org.omnione.did.wallet.v1.agent.api.dto.ProposeEnrollEntityApiResDto;
import org.omnione.did.wallet.v1.agent.api.dto.RequestEcdhApiReqDto;
import org.omnione.did.wallet.v1.agent.api.dto.RequestEcdhApiResDto;
import org.omnione.did.wallet.v1.agent.api.dto.RequestEnrollEntityApiReqDto;
import org.omnione.did.wallet.v1.agent.api.dto.RequestEnrollEntityApiResDto;
import org.omnione.did.wallet.v1.agent.service.query.CertificateVcQueryService;
import org.omnione.did.wallet.v1.agent.api.EnrollFeign;
import org.omnione.did.wallet.v1.agent.dto.EnrollEntityResDto;
import org.springframework.stereotype.Service;

import java.security.interfaces.ECPrivateKey;
import java.util.Arrays;

/**
 * Implementation of the EnrollEntityService interface.
 * This service is used to enroll entities.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EnrollEntityServiceImpl implements EnrollEntityService {
    private final EnrollFeign enrollFeign;
    private final CertificateVcQueryService certificateVcQueryService;
    private final WalletProviderProperty walletProviderProperty;
    private final DidDocService didDocService;
    private final SignatureService signatureService;

    /**
     * Enrolls an entity.
     *
     * @return The response DTO.
     * @throws OpenDidException If an error occurs during the enrollment process.
     */
    @Override
    public EnrollEntityResDto enrollEntity() {
        try {
            log.debug("=== Starting enrollEntity ===");

            // Retrieve Wallet DID Document.
            log.debug("\t--> Retrieving Wallet DID Document");
            DidDocument walletDidDocument = didDocService.getDidDocument(walletProviderProperty.getDid());

            // Send propose-enroll-entity.
            log.debug("\t--> Send propose-enroll-entity");
            ProposeEnrollEntityApiResDto proposeResponse = proposeEnrollEntity();
            String txId = proposeResponse.getTxId();
            String authNonce = proposeResponse.getAuthNonce();

            // Send request-ecdh
            log.debug("\t--> Send request-ecdh");
            EccCurveType eccCurveType = EccCurveType.SECP_256_R1;
            log.debug("\t\t--> generate Tmp Keypair");
            EcKeyPair ecKeyPair = (EcKeyPair) BaseCryptoUtil.generateKeyPair(eccCurveType);
            log.debug("\t\t--> generate ReqEcdh");
            String clientNonce = BaseMultibaseUtil.encode(BaseCryptoUtil.generateNonce(16));
            EcdhReqData reqData = generateReqData(ecKeyPair, eccCurveType, clientNonce, walletDidDocument);
            log.debug("\t\t--> request ECDH");
            RequestEcdhApiResDto ecdhResponse = requestEcdh(txId, reqData);

            // Send request-enroll-entity
            log.debug("\t--> Send request-enroll-entity");
            log.debug("\t\t--> generate DID Auth");
            DidAuth didAuth = generateDidAuth(authNonce, walletDidDocument);
            log.debug("\t\t--> request Enroll Entity");
            RequestEnrollEntityApiResDto enrollEntityResponse = requestEnrollEntity(txId, didAuth);
            log.debug("\t\t--> decrypt VC");
            VerifiableCredential vc = decryptVc((ECPrivateKey) ecKeyPair.getPrivateKey(),
                    ecdhResponse.getAccEcdh(), enrollEntityResponse, clientNonce);

            // Send confirm-enroll-entity
            log.debug("\t--> Send confirm-enroll-entity");
            confirmEnrollEntity(txId, vc.getId());

            // Insert certificate VC.
            log.debug("\t\t--> Insert certificate VC");
            certificateVcQueryService.save(CertificateVc.builder()
                    .vc(vc.toJson())
                    .build());

            log.debug("*** Finished enrollEntity ***");

            return EnrollEntityResDto.builder()
                    .build();
        } catch (OpenDidException e) {
            log.error("Error occurred while enrolling entity: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while enrolling entity: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.TR_ENROLL_ENTITY_FAILED);
        }
    }

    /**
     * Enrolls an entity.
     *
     * @return The response DTO.
     * @throws OpenDidException If an error occurs during the enrollment process.
     */
    private ProposeEnrollEntityApiResDto proposeEnrollEntity() {
        ProposeEnrollEntityApiReqDto request = ProposeEnrollEntityApiReqDto.builder()
                .id(RandomUtil.generateMessageId())
                .build();
        return enrollFeign.proposeEnrollEntityApi(request);
    }

    /**
     * Enrolls an entity.
     *
     * @return The response DTO.
     * @throws OpenDidException If an error occurs during the enrollment process.
     */
    private RequestEcdhApiResDto requestEcdh(String txId, EcdhReqData reqEcdh) {
        RequestEcdhApiReqDto request = RequestEcdhApiReqDto.builder()
                .id(RandomUtil.generateMessageId())
                .txId(txId)
                .reqEcdh(reqEcdh)
                .build();
        try {
            log.debug("Request ECDH: ");
            log.debug(JsonUtil.serializeToJson(request));
        } catch (JsonProcessingException e) {
            log.error("Error occurred while serializing request: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.JSON_PROCESSING_ERROR);
        }
        return enrollFeign.requestEcdh(request);
    }

    /**
     * Generates an ECDH request data object.
     *
     * @param publicKey The public key.
     * @param curveType The curve type.
     * @param clientNonce The client nonce.
     * @param walletDidDocument The wallet DID document.
     * @return The ECDH request data object.
     */
    private EcdhReqData generateReqData(EcKeyPair publicKey, EccCurveType curveType, String clientNonce, DidDocument walletDidDocument) {
        try {
            // Generate Candidate object.
            Candidate candidate = Candidate.builder()
                    .ciphers(Arrays.asList(SymmetricCipherType.values()))
                    .build();

            // Generate Proof object.
            Proof proof = new Proof();
            proof.setType(ProofType.SECP256R1_SIGNATURE_2018.getRawValue());
            proof.setProofPurpose(org.omnione.did.base.datamodel.enums.ProofPurpose.KEY_AGREEMENT.toString());
            proof.setCreated(DateTimeUtil.getCurrentUTCTimeString());
            proof.setVerificationMethod(signatureService.getVerificationMethod(walletDidDocument, org.omnione.did.base.datamodel.enums.ProofPurpose.KEY_AGREEMENT));

            // Generate Signature Object. (except ProofValue)
            EcdhReqData signatureObject = EcdhReqData.builder()
                    .client(walletDidDocument.getId())
                    .clientNonce(clientNonce)
                    .curve(curveType)
                    .publicKey(publicKey.getBase58CompreessPubKey())
                    .candidate(candidate)
                    .proof(proof)
                    .build();

            return signatureService.signEcdhReq(walletDidDocument, signatureObject);
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Requests the enrollment of an entity.
     *
     * @return The response DTO.
     * @throws OpenDidException If an error occurs during the enrollment process.
     */
    private RequestEnrollEntityApiResDto requestEnrollEntity(String txId, DidAuth didAuth) {
        RequestEnrollEntityApiReqDto request = RequestEnrollEntityApiReqDto.builder()
                .id(RandomUtil.generateMessageId())
                .txId(txId)
                .didAuth(didAuth)
                .build();
        return enrollFeign.requestEnrollEntityApi(request);
    }

    /**
     * Generates a DID Auth object.
     *
     * @param authNonce The auth nonce.
     * @param walletDidDocument The wallet DID document.
     * @return The DID Auth object.
     */
    private DidAuth generateDidAuth(String authNonce, DidDocument walletDidDocument) {
        // Generate Proof object.
        Proof proof = new Proof();
        proof.setType(ProofType.SECP256R1_SIGNATURE_2018.getRawValue());
        proof.setProofPurpose(org.omnione.did.base.datamodel.enums.ProofPurpose.AUTHENTICATION.toString());
        proof.setCreated(DateTimeUtil.getCurrentUTCTimeString());
        proof.setVerificationMethod(signatureService.getVerificationMethod(walletDidDocument, ProofPurpose.AUTHENTICATION));

        // Generate Signature Object. (except ProofValue)
        DidAuth signatureObject = DidAuth.builder()
                .authNonce(authNonce)
                .did(walletDidDocument.getId())
                .proof(proof)
                .build();

        return signatureService.signDidAuth(walletDidDocument, signatureObject);
    }

    /**
     * Decrypts a verifiable credential.
     *
     * @param privateKey The private key.
     * @param accEcdh The ECDH object.
     * @param enrollEntityResponse The enrollment response.
     * @param clientNonce The client nonce.
     * @return The decrypted verifiable credential.
     */
    private VerifiableCredential decryptVc(ECPrivateKey privateKey, AccEcdh accEcdh, RequestEnrollEntityApiResDto enrollEntityResponse, String clientNonce) {
        byte[] compressedPublicKey = BaseMultibaseUtil.decode(accEcdh.getPublicKey());
        byte[] sharedSecret = BaseCryptoUtil.generateSharedSecret(compressedPublicKey, privateKey.getEncoded(), EccCurveType.SECP_256_R1);
        byte[] mergeNonce = BaseCryptoUtil.mergeNonce(clientNonce, accEcdh.getServerNonce());
        byte[] mergeSharedSecretAndNonce = BaseCryptoUtil.mergeSharedSecretAndNonce(sharedSecret, mergeNonce, accEcdh.getCipher());

        byte[] iv = BaseMultibaseUtil.decode(enrollEntityResponse.getIv());

        byte[] decrypt = BaseCryptoUtil.decrypt(
                enrollEntityResponse.getEncVc(),
                mergeSharedSecretAndNonce,
                iv,
                accEcdh.getCipher(),
                accEcdh.getPadding()
        );

        String jsonVc = new String(decrypt);

        VerifiableCredential vc = new VerifiableCredential();
        vc.fromJson(jsonVc);

        return vc;
    }

    /**
     * Confirms the enrollment of an entity.
     *
     * @param txId The transaction ID.
     * @param vcId The VC ID.
     * @return The response DTO.
     * @throws OpenDidException If an error occurs during the enrollment process.
     */
    private ConfirmEnrollEntityApiResDto confirmEnrollEntity(String txId, String vcId) {
        ConfirmEnrollEntityApiReqDto request = ConfirmEnrollEntityApiReqDto.builder()
                .id(RandomUtil.generateMessageId())
                .txId(txId)
                .vcId(vcId)
                .build();

        return enrollFeign.confirmEnrollEntityApi(request);
    }
}

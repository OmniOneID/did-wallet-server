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
import org.omnione.did.base.datamodel.data.AttestedDidDoc;
import org.omnione.did.base.datamodel.data.DidAuth;
import org.omnione.did.base.datamodel.data.EcdhReqData;
import org.omnione.did.base.datamodel.enums.ProofPurpose;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.property.WalletProviderProperty;
import org.omnione.did.base.utils.BaseCoreDidUtil;
import org.omnione.did.base.utils.BaseMultibaseUtil;
import org.omnione.did.common.util.JsonUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.did.VerificationMethod;
import org.springframework.stereotype.Service;

/**
 * Service for signing data.
 * This class provides methods for signing data and generating proofs.
 * This class is used to sign attested DID documents, ECDH requests, and DID Auth objects.
 * It also provides methods for generating verification methods.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class SignatureService {
    private final DidDocService didDocService;
    private final FileWalletService fileWalletService;
    private final WalletProviderProperty walletProviderProperty;

    /**
     * Sign the given attested DID document and generate a proof.
     *
     * @param walletDidDocument the wallet DID document to sign
     * @param signatureObject the attested DID document to sign
     * @return the signed attested DID document
     * @throws OpenDidException if the signature generation fails
     */
    public AttestedDidDoc signAttestedDidDoc(DidDocument walletDidDocument, AttestedDidDoc signatureObject) {
        try {
            // Serialize and sort data set.
            String serializedJson = JsonUtil.serializeAndSort(signatureObject);

            // Sign data.
            String proofValue = signData(walletDidDocument, serializedJson, ProofPurpose.fromDisplayName(signatureObject.getProof().getProofPurpose()));

            // Generate proof.
            Proof proof = new Proof();
            proof.setType(signatureObject.getProof().getType());
            proof.setCreated(signatureObject.getProof().getCreated());
            proof.setVerificationMethod(signatureObject.getProof().getVerificationMethod());
            proof.setProofPurpose(signatureObject.getProof().getProofPurpose());
            proof.setProofValue(proofValue);

            return AttestedDidDoc.builder()
                    .walletId(signatureObject.getWalletId())
                    .nonce(signatureObject.getNonce())
                    .ownerDidDoc(signatureObject.getOwnerDidDoc())
                    .provider(signatureObject.getProvider())
                    .did(walletDidDocument.getId())
                    .proof(proof)
                    .build();

        } catch (JsonProcessingException e) {
            log.error("Json Processing error: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.JSON_PROCESSING_ERROR);
        } catch (Exception e) {
            log.error("Error occurred while signing attested DID document: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.SIGNATURE_GENERATION_FAILED);
        }
    }

    /**
     * Sign the given data set and generate a proof.
     *
     * @param casDidDocument the DID Document of the CAS
     * @param signatureMessage the data set to sign
     * @param proofPurpose the proof purpose
     * @return the proof value
     * @throws OpenDidException if the signature generation fails
     */
    private String signData(DidDocument casDidDocument, String signatureMessage, ProofPurpose proofPurpose) {
        // Get the key ID
        VerificationMethod verificationMethod = BaseCoreDidUtil.getVerificationMethod(casDidDocument, proofPurpose.toKeyId());
        String keyId = verificationMethod.getId();

        //  Sign the message.
        byte[] signature = fileWalletService.generateCompactSignature(keyId, signatureMessage);

        return BaseMultibaseUtil.encode(signature);
    }

    /**
     * Get the verification method for the given Wallet DID Document and proof purpose.
     *
     * @param walletDidDoc the Own DID Document to use
     * @param proofPurpose the purpose of the proof
     * @return the verification method
     */
    public String getVerificationMethod(DidDocument walletDidDoc, ProofPurpose proofPurpose) {
        String version = walletDidDoc.getVersionId();
        VerificationMethod verificationMethod = BaseCoreDidUtil.getVerificationMethod(walletDidDoc, proofPurpose.toKeyId());

        return walletDidDoc.getId() + "?versionId=" + version + "#" + verificationMethod.getId();
    }

    /**
     * Sign the given ECDH request and generate a proof.
     *
     * @param walletDidDocument the DID Document of the wallet
     * @param signatureObject the ECDH request to sign
     * @return the signed ECDH request
     * @throws OpenDidException if the signature generation fails
     */
    public EcdhReqData signEcdhReq(DidDocument walletDidDocument, EcdhReqData signatureObject) {
        try {
            // Serialize and sort data set.
            String serializedJson = JsonUtil.serializeAndSort(signatureObject);

            // Sign data.
            String proofValue = signData(walletDidDocument, serializedJson, ProofPurpose.fromDisplayName(signatureObject.getProof().getProofPurpose()));

            // Generate proof.
            Proof proof = new Proof();
            proof.setType(signatureObject.getProof().getType());
            proof.setCreated(signatureObject.getProof().getCreated());
            proof.setVerificationMethod(signatureObject.getProof().getVerificationMethod());
            proof.setProofPurpose(signatureObject.getProof().getProofPurpose());
            proof.setProofValue(proofValue);

            return EcdhReqData.builder()
                    .client(walletDidDocument.getId())
                    .clientNonce(signatureObject.getClientNonce())
                    .curve(signatureObject.getCurve())
                    .publicKey(signatureObject.getPublicKey())
                    .candidate(signatureObject.getCandidate())
                    .proof(proof)
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Json Processing error: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.JSON_PROCESSING_ERROR);
        } catch (Exception e) {
            log.error("Error occurred while signing ECDH request: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.SIGNATURE_GENERATION_FAILED);
        }
    }

    /**
     * Sign the given DID Auth object and generate a proof.
     *
     * @param walletDidDocument the DID Document of the wallet
     * @param signatureObject the DID Auth object to sign
     * @return the signed DID Auth object
     * @throws OpenDidException if the signature generation fails
     */
    public DidAuth signDidAuth(DidDocument walletDidDocument, DidAuth signatureObject) {
        try {
            // Serialize and sort data set.
            String serializedJson = JsonUtil.serializeAndSort(signatureObject);

            // Sign data.
            String proofValue = signData(walletDidDocument, serializedJson, ProofPurpose.fromDisplayName(signatureObject.getProof().getProofPurpose()));

            // Generate proof.
            Proof proof = new Proof();
            proof.setType(signatureObject.getProof().getType());
            proof.setCreated(signatureObject.getProof().getCreated());
            proof.setVerificationMethod(signatureObject.getProof().getVerificationMethod());
            proof.setProofPurpose(signatureObject.getProof().getProofPurpose());
            proof.setProofValue(proofValue);

            return DidAuth.builder()
                    .authNonce(signatureObject.getAuthNonce())
                    .did(walletDidDocument.getId())
                    .proof(proof)
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Json Processing error: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.JSON_PROCESSING_ERROR);
        } catch (Exception e) {
            log.error("Error occurred while signing DID Auth: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.SIGNATURE_GENERATION_FAILED);
        }
    }
}

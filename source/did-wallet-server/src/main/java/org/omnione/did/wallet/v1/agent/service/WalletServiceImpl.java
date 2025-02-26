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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.datamodel.data.AttestedDidDoc;
import org.omnione.did.base.datamodel.enums.ProofPurpose;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.property.WalletProviderProperty;
import org.omnione.did.base.utils.BaseMultibaseUtil;
import org.omnione.did.base.utils.RandomUtil;
import org.omnione.did.common.util.DateTimeUtil;
import org.omnione.did.common.util.JsonUtil;
import org.omnione.did.core.exception.CoreException;
import org.omnione.did.core.manager.DidManager;
import org.omnione.did.crypto.enums.MultiBaseType;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.data.model.enums.did.ProofType;
import org.omnione.did.data.model.provider.Provider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Implementation of the WalletService interface.
 * This service handles wallet-related operations, including signing and verifying DID documents.
 */
@Slf4j
@RequiredArgsConstructor
@Profile("!sample")
@Service
public class WalletServiceImpl implements WalletService {
    private final SignatureService signatureService;
    private final DidDocService didDocService;
    private final WalletProviderProperty walletProviderProperty;

    /**
     * Signs a wallet by creating an attested DID document.
     *
     * @param didDocument The DID document to be signed.
     * @return An attested DID document.
     * @throws OpenDidException If an error occurs during the signing process.
     */
    @Override
    public AttestedDidDoc signWallet(DidDocument didDocument) {
        try {
            log.debug("=== Starting signWallet ===");
            log.debug("request: {}", didDocument.toJson());

            // Verify signature.
            log.debug("\t--> Verifying signature");
            verifySignature(didDocument);

            // Generate Wallet ID.
            log.debug("\t--> Generating Wallet ID");
            String walletId = RandomUtil.generateWalletId();

            // Generate Attested DID Document.
            log.debug("\t--> Generating Attested DID Document");
            AttestedDidDoc attestedDidDoc = generateAttestedDidDoc(walletId, didDocument);

            log.debug("*** Finished signWallet ***");
            log.debug("response: {}", JsonUtil.serializeToJson(attestedDidDoc));

            return attestedDidDoc;
        } catch (OpenDidException e) {
            log.error("Error occurred while signing wallet: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while signing wallet: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.TR_SIGN_WALLET_FAILED);
        }
    }

    /**
     * Generates an attested DID document.
     *
     * @param walletId The ID of the wallet.
     * @param didDocument The original DID document.
     * @return An attested DID document.
     * @throws OpenDidException signature generation fails.
     */
    private AttestedDidDoc generateAttestedDidDoc(String walletId, DidDocument didDocument) {
        try {
            // Retrieve Wallet DID Document.
            log.debug("\t--> Retrieving Wallet DID Document");
            DidDocument walletDidDocument = didDocService.getDidDocument(walletProviderProperty.getDid());

            // Generate signature object.
            log.debug("\t--> Generating Signature Object");
            AttestedDidDoc signatureObject = generateSignatureMessage(walletId, didDocument, walletDidDocument);

            // Sign data.
            log.debug("\t--> Signing Data");
            return signatureService.signAttestedDidDoc(walletDidDocument, signatureObject);
        } catch (OpenDidException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to generate attested DID document: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.SIGNATURE_GENERATION_FAILED);
        }
    }

    /**
     * Generates a signature message for the attested DID document.
     *
     * @param walletId The ID of the wallet.
     * @param holderDidDocument The DID document of the holder.
     * @param walletDidDocument The DID document of the wallet.
     * @return An AttestedDidDoc object containing the signature message.
     * @throws OpenDidException signature generation fails.
     */
    private AttestedDidDoc generateSignatureMessage(String walletId, DidDocument holderDidDocument, DidDocument walletDidDocument) {
        try {
            // Generate Provider object.
            Provider provider = new Provider();
            provider.setDid(walletProviderProperty.getDid());
            provider.setCertVcRef(walletProviderProperty.getCertificateVc());

            // Encode DID Document.
            String encodedDidDocument = BaseMultibaseUtil.encode(holderDidDocument.toJson().getBytes(StandardCharsets.UTF_8), MultiBaseType.base64url);

            // Generate Proof object.
            Proof proof = new Proof();
            proof.setType(ProofType.SECP256R1_SIGNATURE_2018.getRawValue());
            proof.setProofPurpose(ProofPurpose.ASSERTION_METHOD.toString());
            proof.setCreated(DateTimeUtil.getCurrentUTCTimeString());
            proof.setVerificationMethod(signatureService.getVerificationMethod(walletDidDocument, ProofPurpose.ASSERTION_METHOD));

            return AttestedDidDoc.builder()
                    .walletId(walletId)
                    .ownerDidDoc(encodedDidDocument)
                    .provider(provider)
                    .nonce(RandomUtil.generateNonce())
                    .proof(proof)
                    .build();
        } catch (OpenDidException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to generate signature message: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.SIGNATURE_GENERATION_FAILED);
        }
    }

    /**
     * Verifies the signature of a DID document.
     *
     * @param didDocument The DID document to be verified.
     * @throws OpenDidException If the signature verification fails.
     */
    private void verifySignature(DidDocument didDocument) {
        try {
            DidManager didManager = new DidManager();
            didManager.parse(didDocument.toJson());
            didManager.verifyDocumentSignature();
        } catch (CoreException e) {
            log.error("Error occurred while verifying signature: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.CRYPTO_VERIFY_SIGN_FAILED);
        }
    }
}
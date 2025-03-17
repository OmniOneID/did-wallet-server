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

package org.omnione.did.base.exception;

import lombok.Getter;

/**
 * Enumeration of error codes used in the wallet backend.
 * Each error code contains a unique identifier, a descriptive message, and an associated HTTP status code.
 *
 */
@Getter
public enum ErrorCode {
    // Transaction Errors (001xx)
    TR_ENROLL_ENTITY_FAILED("00101", "Failed to Enroll Entity.", 500),
    TR_GET_CERTIFICATE_VC_FAILED("00102", "Failed to Get Certificate VC.", 500),
    TR_SIGN_WALLET_FAILED("00103", "Failed to request sign wallet.", 500),

    // Cryptography and Security Errors (002xx)
    CRYPTO_SYMMETRIC_CIPHER_TYPE_INVALID("00201", "Invalid symmetric cipher type.", 500),
    CRYPTO_PROOF_PURPOSE_INVALID("00202", "Invalid proof purpose.", 400),
    CRYPTO_VERIFY_SIGN_FAILED("00203", "Verify Signature Failed.", 400),
    KEY_PAIR_GENERATION_FAILED("00204", "Failed to generate key pair.", 500),
    NONCE_GENERATION_FAILED("00205", "Failed to generate nonce.", 500),
    NONCE_MERGE_FAILED("00206", "Failed to merge nonce.", 500),
    INITIAL_VECTOR_GENERATION_FAILED("00207", "Failed to generate initial vector.", 500),
    SESSION_KEY_GENERATION_FAILED("00208", "Failed to generate session key.", 500),
    NONCE_AND_SHARED_SECRET_MERGE_FAILED("00209", "Failed to merge nonce and shared secret.", 500),
    ENCRYPTION_FAILED("00210", "Failed to encrypt data.", 500),
    DECRYPTION_FAILED("00211", "Failed to decrypt data.", 500),
    PUBLIC_KEY_COMPRESS_FAILED("00212", "Failed to compress public key.", 500),
    HASH_GENERATION_FAILED("00213", "Failed to generate hash value.", 500),
    ENCODING_FAILED("00214", "Failed to encode data.", 500),
    DECODING_FAILED("00215", "Failed to decode data: incorrect encoding.", 400),

    // DID and VC Related Errors (003xx)
    CERTIFICATE_DATA_NOT_FOUND("00301", "Certificate VC data not found.", 500),
    FIND_DID_DOC_FAILURE("00302", "Failed to find DID Document.", 500),
    DID_DOCUMENT_RETRIEVAL_FAILED("00303", "Failed to retrieve DID Document.", 500),
    VERIFY_DIDDOC_KEY_PROOF_FAILED("00304", "Failed to verify DID document key proof.", 400),

    // Wallet Errors (004xx)
    WALLET_CONNECTION_FAILED("00401", "Failed to connect to wallet.", 500),
    WALLET_SIGNATURE_GENERATION_FAILED("00402", "Failed to generate wallet signature.", 500),
    SIGNATURE_GENERATION_FAILED("00403", "Failed to generate signature.", 500),
    WALLET_CREATION_FAILED("00404", "Failed to create wallet.", 500),
    FAILED_TO_GET_FILE_WALLET_MANAGER("00405", "Failed to get File Wallet Manager", 500),

    // General and Server Errors (005xx)
    JSON_PROCESSING_ERROR("00501", "Error occurred while processing JSON data.", 400),
    REQUEST_BODY_UNREADABLE("00502", "Unable to process the request.", 400),
    SERVER_ERROR("00503", "Server Error", 500),

    // Blockchain Errors (006xx)
    BLOCKCHAIN_DIDDOC_REGISTRATION_FAILED("00601", "Failed to register DID Document on the blockchain.", 500),
    BLOCKCHAIN_GET_DID_DOC_FAILED("00602", "Failed to retrieve DID document on the blockchain.", 500),
    BLOCKCHAIN_UPDATE_DID_DOC_FAILED("00603", "Failed to update DID Document on the blockchain.", 500),
    BLOCKCHAIN_VC_META_REGISTRATION_FAILED("00604", "Failed to register VC meta on the blockchain.", 500),
    BLOCKCHAIN_VC_META_RETRIEVAL_FAILED("00605", "Failed to retrieve VC meta on the blockchain.", 500),
    BLOCKCHAIN_VC_STATUS_UPDATE_FAILED("00606", "Failed to update VC status on the blockchain.", 500),
    BLOCKCHAIN_TO_REMOVE_INDEX_FAILED("00607", "Failed to remove index on the blockchain", 500),

    // Admin Errors (007xx)
    ADMIN_INFO_NOT_FOUND("00701", "Failed to find admin: admin is not registered.", 400),
    ADMIN_ALREADY_EXISTS("00702", "Failed to register admin: admin is already registered.", 400),

    // Wallet Service Errors (008xx)
    WALLET_SERVICE_INFO_NOT_FOUND("00801", "Failed to find wallet service: wallet service is not registered.", 400),

    // Wallet Management Errors (009xx)
    WALLET_INFO_NOT_FOUND("00901", "Failed to find wallet: wallet is not registered.", 400)
    ;

    private final String code;
    private final String message;
    private final int httpStatus;

    /**
     * Constructor for ErrorCode enum.
     *
     * @param code       Error Code
     * @param message    Error Message
     * @param httpStatus HTTP Status Code
     */
    ErrorCode(String code, String message, int httpStatus) {
        this.code = "S" + "SRV" + "WLT" + code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * Get the error code.
     *
     * @return Error Code
     */
    public static String getMessageByCode(String code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode.getMessage();
            }
        }
        return "Unknown error code: " + code;
    }
}

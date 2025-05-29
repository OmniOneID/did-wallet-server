---
puppeteer:
    pdf:
        format: A4
        displayHeaderFooter: true
        landscape: false
        scale: 0.8
        margin:
            top: 1.2cm
            right: 1cm
            bottom: 1cm
            left: 1cm
    image:
        quality: 100
        fullPage: false
---

Wallet Server Error
==

- Date: 2024-09-03
- Version: v1.0.0

| Version          | Date       | Changes                  |
| ---------------- | ---------- | ------------------------ |
| v1.0.0  | 2024-09-03 | Initial version          |

<div style="page-break-after: always;"></div>

# Table of Contents
- [Wallet Server Error](#wallet-server-error)
- [Table of Contents](#table-of-contents)
- [Model](#model)
  - [Error Response](#error-response)
    - [Description](#description)
    - [Declaration](#declaration)
    - [Property](#property)
- [Error Code](#error-code)
  - [1. Transaction Errors (001xx)](#1-transaction-errors-001xx)
  - [2. Cryptography and Security Errors (002xx)](#2-cryptography-and-security-errors-002xx)
  - [3. DID and VC Related Errors (003xx)](#3-did-and-vc-related-errors-003xx)
  - [4. Wallet Errors (004xx)](#4-wallet-errors-004xx)
  - [5. General and Server Errors (005xx)](#5-general-and-server-errors-005xx)
  - [6. Blockchain Errors (006xx)](#6-blockchain-errors-006xx)
  - [7. Admin Errors (007xx)](#7-admin-errors-007xx)

# Model
## Error Response

### Description
```
Error struct for Server. It has code and message.
```

### Declaration
```java
public class ErrorResponse {
    private final String code;
    private final String message;
}
```

### Property

| Name        | Type   | Description                   | **M/O** | **Note** |
|-------------|--------|-------------------------------|---------| -------- |
| code        | String | Error code                    | M       |          | 
| message     | String | Error description             | M       |          |

# Error Code

## 1. Transaction Errors (001xx)

| Error Code       | Error Message                        | Description | Action Required                            |
|------------------|--------------------------------------|-------------|--------------------------------------------|
| SSRVWLT00101     | Failed to Enroll Entity.             | -           | Check entity enrollment process.           |
| SSRVWLT00102     | Failed to Get Certificate VC.        | -           | Verify Certificate VC retrieval process.   |
| SSRVWLT00103     | Failed to request sign wallet.       | -           | Check wallet signing process.              |

## 2. Cryptography and Security Errors (002xx)

| Error Code       | Error Message                                           | Description | Action Required                                |
|------------------|-------------------------------------------------------- |-------------|------------------------------------------------|
| SSRVWLT00201     | Invalid symmetric cipher type.                          | -           | Verify the symmetric cipher type.              |
| SSRVWLT00202     | Invalid proof purpose.                                  | -           | Check the proof purpose.                       |
| SSRVWLT00203     | Verify Signature Failed.                                | -           | Review the signature verification process.     |
| SSRVWLT00204     | Failed to generate key pair.                            | -           | Check key pair generation process.             |
| SSRVWLT00205     | Failed to generate nonce.                               | -           | Verify nonce generation process.               |
| SSRVWLT00206     | Failed to merge nonce.                                  | -           | Check nonce merging process.                   |
| SSRVWLT00207     | Failed to generate initial vector.                      | -           | Verify initial vector generation process.      |
| SSRVWLT00208     | Failed to generate session key.                         | -           | Check session key generation process.          |
| SSRVWLT00209     | Failed to merge nonce and shared secret.                | -           | Verify nonce and shared secret merging process.|
| SSRVWLT00210     | Failed to encrypt data.                                 | -           | Check encryption process and parameters.       |
| SSRVWLT00211     | Failed to decrypt data.                                 | -           | Verify decryption process and keys.            |
| SSRVWLT00212     | Failed to compress public key.                          | -           | Check public key compression algorithm.        |
| SSRVWLT00213     | Failed to generate hash value.                          | -           | Verify hash generation process.                |
| SSRVWLT00214     | Failed to encode data.                                  | -           | Check encoding process and input data.         |
| SSRVWLT00215     | Failed to decode data: incorrect encoding.              | -           | Verify encoded data and decoding method.       |
| SSRVWLT00216 | Failed to generate keys: key already exists. | -           | Check if the key already exists. Avoid duplication before generation. |

## 3. DID and VC Related Errors (003xx)

| Error Code       | Error Message                                  | Description | Action Required                                |
|------------------|------------------------------------------------|-------------|------------------------------------------------|
| SSRVWLT00301     | Certificate VC data not found.                 | -           | Check Certificate VC data existence and storage.|
| SSRVWLT00302     | Failed to find DID Document.                   | -           | Verify DID Document lookup process.            |
| SSRVWLT00303     | Failed to retrieve DID Document.               | -           | Check DID Document retrieval process.          |
| SSRVWLT00304     | Failed to verify DID document key proof.       | -           | Verify DID document key proof process.         |

## 4. Wallet Errors (004xx)

| Error Code       | Error Message                                  | Description | Action Required                                |
|------------------|------------------------------------------------|-------------|------------------------------------------------|
| SSRVWLT00401     | Failed to connect to wallet.                   | -           | Check wallet connection and network status.    |
| SSRVWLT00402     | Failed to generate wallet signature.           | -           | Verify wallet signature generation process.    |
| SSRVWLT00403     | Failed to generate signature.                  | -           | Check signature generation process.            |
| SSRVWLT00404     | Failed to create wallet.                       | -           | Verify wallet creation process.                |
| SSRVWLT00405     | Failed to get File Wallet Manager              | -           | Check File Wallet Manager initialization.      |
| SSRVWLT00406 | Failed to create wallet: wallet already exists. | -         | Ensure the wallet doesn't already exist before creation. |
| SSRVWLT00407 | Invalid proof purpose.                        | -           | Verify that the proof purpose is valid and correctly set. |

## 5. General and Server Errors (005xx)

| Error Code       | Error Message                                  | Description | Action Required                                |
|------------------|------------------------------------------------|-------------|------------------------------------------------|
| SSRVWLT00501     | Error occurred while processing JSON data.     | -           | Check JSON data format and processing.         |
| SSRVWLT00502     | Unable to process the request.                 | -           | Verify request format and content.             |
| SSRVWLT00503     | Server Error                                   | -           | Check server logs and configuration.           |

## 6. Blockchain Errors (006xx)

| Error Code       | Error Message                                           | Description | Action Required                                |
|------------------|-------------------------------------------------------- |-------------|------------------------------------------------|
| SSRVWLT00601     | Failed to register DID Document on the blockchain.      | -           | Check blockchain registration process for DID. |
| SSRVWLT00602     | Failed to retrieve DID document on the blockchain.      | -           | Verify blockchain retrieval process for DID.   |
| SSRVWLT00603     | Failed to update DID Document on the blockchain.        | -           | Check blockchain update process for DID.       |
| SSRVWLT00604     | Failed to register VC meta on the blockchain.           | -           | Verify VC meta registration on blockchain.     |
| SSRVWLT00605     | Failed to retrieve VC meta on the blockchain.           | -           | Check VC meta retrieval from blockchain.       |
| SSRVWLT00606     | Failed to update VC status on the blockchain.           | -           | Verify VC status update process on blockchain. |
| SSRVWLT00607     | Failed to remove index on the blockchain                | -           | Check index removal process on blockchain.     |


## 7. Admin Errors (007xx)

| Error Code   | Error Message                                | Description | Action Required                          |
|--------------|-----------------------------------------------|-------------|------------------------------------------|
| SSRVWLT00701 | Failed to find admin: admin is not registered. | -          | Ensure the admin is properly registered. |
| SSRVWLT00702 | Failed to register admin: admin is already registered. | -     | Check for duplicate registration before proceeding. |

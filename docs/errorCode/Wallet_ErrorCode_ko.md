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
| v1.0.0  | 2024-09-03 | 최초 버전          |

<div style="page-break-after: always;"></div>

# 목차
- [Model](#model)
  - [Error Response](#error-response)
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

### 설명
```
Wallet 백엔드를 위한 에러코드입니다. 코드와 메시지 쌍을 가지고 있습니다.
코드는 SSRVWLT로 시작합니다.
```

### 선언
```java
public class ErrorResponse {
    private final String code;
    private final String message;
}
```

### 속성

| Name        | Type   | Description                   | **M/O** | **Note** |
|-------------|--------|-------------------------------|---------| -------- |
| code        | String | 에러 코드                    | M       |          | 
| message     | String | 에러 설명             | M       |          |

# Error Code

## 1. Transaction Errors (001xx)

| Error Code       | Error Message                        | Description | Action Required                            |
|------------------|--------------------------------------|-------------|-------------------------------------------|
| SSRVWLT00101     | Failed to Enroll Entity.             | -           | 엔터티 등록 프로세스를 확인하세요.           |
| SSRVWLT00102     | Failed to Get Certificate VC.        | -           | Certificate VC 검색 프로세스를 확인하세요.   |
| SSRVWLT00103     | Failed to request sign wallet.       | -           | 지갑 서명 프로세스를 확인하세요.              |

## 2. Cryptography and Security Errors (002xx)

| Error Code       | Error Message                                           | Description | Action Required                                |
|------------------|-------------------------------------------------------- |-------------|------------------------------------------------|
| SSRVWLT00201     | Invalid symmetric cipher type.                          | -           | 대칭 암호화 타입을 확인하세요.              |
| SSRVWLT00202     | Invalid proof purpose.                                  | -           | 증명 목적을 확인하세요.                       |
| SSRVWLT00203     | Verify Signature Failed.                                | -           | 서명 검증 프로세스를 검토하세요.     |
| SSRVWLT00204     | Failed to generate key pair.                            | -           | 키 페어 생성 프로세스를 확인하세요.             |
| SSRVWLT00205     | Failed to generate nonce.                               | -           | 논스 생성 프로세스를 확인하세요.               |
| SSRVWLT00206     | Failed to merge nonce.                                  | -           | 논스 병합 프로세스를 확인하세요.                   |
| SSRVWLT00207     | Failed to generate initial vector.                      | -           | 초기 벡터 생성 프로세스를 확인하세요.      |
| SSRVWLT00208     | Failed to generate session key.                         | -           | 세션 키 생성 프로세스를 확인하세요.          |
| SSRVWLT00209     | Failed to merge nonce and shared secret.                | -           | 논스와 공유 비밀 병합 프로세스를 확인하세요.|
| SSRVWLT00210     | Failed to encrypt data.                                 | -           | 암호화 프로세스와 매개변수를 확인하세요.       |
| SSRVWLT00211     | Failed to decrypt data.                                 | -           | 복호화 프로세스와 키를 확인하세요.            |
| SSRVWLT00212     | Failed to compress public key.                          | -           | 공개 키 압축 알고리즘을 확인하세요.        |
| SSRVWLT00213     | Failed to generate hash value.                          | -           | 해시 생성 프로세스를 확인하세요.                |
| SSRVWLT00214     | Failed to encode data.                                  | -           | 인코딩 프로세스와 입력 데이터를 확인하세요.         |
| SSRVWLT00215     | Failed to decode data: incorrect encoding.              | -           | 인코딩된 데이터와 디코딩 방법을 확인하세요.       |
| SSRVWLT00216 | Failed to generate keys: key already exists. | -           | 키가 이미 존재하는지 확인하세요. 생성 전에 중복을 방지하세요. |

## 3. DID and VC Related Errors (003xx)

| Error Code       | Error Message                                  | Description | Action Required                                |
|------------------|------------------------------------------------|-------------|------------------------------------------------|
| SSRVWLT00301     | Certificate VC data not found.                 | -           | Certificate VC 데이터 존재 여부와 저장소를 확인하세요.|
| SSRVWLT00302     | Failed to find DID Document.                   | -           | DID 문서 조회 프로세스를 확인하세요.            |
| SSRVWLT00303     | Failed to retrieve DID Document.               | -           | DID 문서 검색 프로세스를 확인하세요.          |
| SSRVWLT00304     | Failed to verify DID document key proof.       | -           | DID 문서 키 증명 프로세스를 확인하세요.         |

## 4. Wallet Errors (004xx)

| Error Code       | Error Message                                  | Description | Action Required                                |
|------------------|------------------------------------------------|-------------|------------------------------------------------|
| SSRVWLT00401     | Failed to connect to wallet.                   | -           | 지갑 연결과 네트워크 상태를 확인하세요.    |
| SSRVWLT00402     | Failed to generate wallet signature.           | -           | 지갑 서명 생성 프로세스를 확인하세요.    |
| SSRVWLT00403     | Failed to generate signature.                  | -           | 서명 생성 프로세스를 확인하세요.            |
| SSRVWLT00404     | Failed to create wallet.                       | -           | 지갑 생성 프로세스를 확인하세요.                |
| SSRVWLT00405     | Failed to get File Wallet Manager              | -           | File Wallet Manager 초기화를 확인하세요.      |
| SSRVWLT00406 | Failed to create wallet: wallet already exists. | -         | 생성 전에 지갑이 이미 존재하지 않는지 확인하세요. |
| SSRVWLT00407 | Invalid proof purpose.                        | -           | 증명 목적이 유효하고 올바르게 설정되었는지 확인하세요. |

## 5. General and Server Errors (005xx)

| Error Code       | Error Message                                  | Description | Action Required                                |
|------------------|------------------------------------------------|-------------|------------------------------------------------|
| SSRVWLT00501     | Error occurred while processing JSON data.     | -           | JSON 데이터 형식과 처리를 확인하세요.         |
| SSRVWLT00502     | Unable to process the request.                 | -           | 요청 형식과 내용을 확인하세요.             |
| SSRVWLT00503     | Server Error                                   | -           | 서버 로그와 구성을 확인하세요.           |

## 6. Blockchain Errors (006xx)

| Error Code       | Error Message                                           | Description | Action Required                                |
|------------------|-------------------------------------------------------- |-------------|------------------------------------------------|
| SSRVWLT00601     | Failed to register DID Document on the blockchain.      | -           | DID의 블록체인 등록 프로세스를 확인하세요. |
| SSRVWLT00602     | Failed to retrieve DID document on the blockchain.      | -           | DID의 블록체인 검색 프로세스를 확인하세요.   |
| SSRVWLT00603     | Failed to update DID Document on the blockchain.        | -           | DID의 블록체인 업데이트 프로세스를 확인하세요.       |
| SSRVWLT00604     | Failed to register VC meta on the blockchain.           | -           | 블록체인에서 VC 메타 등록을 확인하세요.     |
| SSRVWLT00605     | Failed to retrieve VC meta on the blockchain.           | -           | 블록체인에서 VC 메타 검색을 확인하세요.       |
| SSRVWLT00606     | Failed to update VC status on the blockchain.           | -           | 블록체인에서 VC 상태 업데이트 프로세스를 확인하세요. |
| SSRVWLT00607     | Failed to remove index on the blockchain                | -           | 블록체인에서 인덱스 제거 프로세스를 확인하세요.     |


## 7. Admin Errors (007xx)

| Error Code   | Error Message                                | Description | Action Required                          |
|--------------|-----------------------------------------------|-------------|------------------------------------------|
| SSRVWLT00701 | Failed to find admin: admin is not registered. | -          | 관리자가 제대로 등록되었는지 확인하세요. |
| SSRVWLT00702 | Failed to register admin: admin is already registered. | -     | 진행하기 전에 중복 등록을 확인하세요. |
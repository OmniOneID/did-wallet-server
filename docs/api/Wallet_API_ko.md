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

Wallet API
==

- 일자: 2024-08-19
- 버전: v1.0.0
  
목차
---
<!-- TOC tocDepth:2..3 chapterDepth:2..6 -->

- [1. 개요](#1-개요)
- [2. 용어 설명](#2-용어-설명)
- [3. API 목록](#3-api-목록)
    - [3.1. 순차 API](#31-순차-api)
    - [3.2. 단일호출 API](#32-단일호출-api)
- [4. 단일 호출 API](#4-단일-호출-api)
    - [4.1. Issue Certificate VC](#41-issue-certificate-vc)
    - [4.2. Get Certificate Vc](#42-get-certificate-vc)
    - [4.3. Request Sign Wallet](#43-request-sign-wallet)

<!-- /TOC -->


## 1. 개요

본 문서는 Wallet Service가 제공하는 API를 정의한다.

<div style="page-break-after: always; margin-top: 50px;"></div>

## 2. 용어 설명
- 프로토콜 (Protocol)
  - 특정 기능을 수행하기 위해 정해진 순서에 따라 호출해야 하는 `순차 API`의 집합이다. API 호출 순서를 엄격히 따라야 하며, 순서가 잘못될 경우 예상하지 못한 결과가 발생할 수 있다.
- 순차 API (Sequential API)
  - 특정 기능(프로토콜)을 수행하기 위해 정해진 순서대로 호출하는 일련의 API를 말한다. 각 API는 순차적으로 호출되어야 하며, 순서가 잘못될 경우 제대로 동작하지 않을 수 있다.
  - 그러나 일부 프로토콜에서는 같은 호출 순서를 가진 API가 존재할 수 있으며, 이 경우 하나의 API를 선택하여 호출할 수 있다.
- 단일 호출 API (Single Call API)
  - 일반적인 REST API처럼 순서에 관계없이 독립적으로 호출 가능한 API를 의미한다.
- 표준 API (Standard API)
  - API 문서에서 명확하게 정의된 API로, 모든 구현체에서 일관된 방식으로 제공되어야 한다. 표준 API는 시스템 간 상호 운용성을 보장하며, 사전에 정의된 스펙에 따라 동작해야 한다.
- 비표준 API (Non-Standard API)
  - 구현체마다 필요에 따라 다르게 정의되거나 커스터마이징될 수 있는 API이다. 본 문서에서 제공하는 비표준 API는 한 가지 예시일 뿐이며, 각 구현체에 맞춰 다르게 구현될 수 있다. 이 경우, 구현체별 별도의 문서화가 필요하다.
  - 예를 들어, DID Document 서명은 시스템에 따라 구현 방법이 달라질 수 있으며, `request-sign-wallet` API와 같은 비표준 API는 각 구현체에서 필요한 방식으로 재정의할 수 있다.

<div style="page-break-after: always; margin-top: 50px;"></div>

## 3. API 목록

### 3.1. 순차 API
Wallet Service는 현재 특정 기능을 수행하기 위한 프로토콜이 정의되어 있지 않으며, 따라서 순차 API도 제공하지 않는다.

<div style="page-break-after: always; margin-top: 40px;"></div>

### 3.2. 단일호출 API

| API                    | URL                         | Description            | 표준API |
| ---------------------- | --------------------------- | ---------------------- | ------- |
| `issue-certificate-vc` | /api/v1/certificate-vc      | Entity 등록 요청       | N       |
| `get-certificate-vc`   | /api/v1/certificate-vc      | 가입증명서 조회        | N       |
| `request-sign-wallet`  | /api/v1/request-sign-wallet | DID Document 서명 요청 | N       |

<div style="page-break-after: always; margin-top: 50px;"></div>

## 4. 단일 호출 API

단일 호출 API는 특정 기능을 수행하는 하나의 독립된 API이다.
따라서 순서대로 호출해야 하는 API의 집단인 순차 API(aka, 프로토콜)이 아니므로 프로토콜 번호가 부여되지 않는다.
Wallet Service가 제공하는 단일 호출 API 목록은 아래 표와 같다.

| API                    | URL                         | Description            | 표준API |
| ---------------------- | --------------------------- | ---------------------- | ------- |
| `issue-certificate-vc` | /api/v1/certificate-vc      | Entity 등록 요청       | N       |
| `get-certificate-vc`   | /api/v1/certificate-vc      | 가입증명서 조회        | N       |
| `request-sign-wallet`  | /api/v1/request-sign-wallet | DID Document 서명 요청 | N       |

■ Authorization

프로토콜에는 '호출자의 호출 권한을 확인'(authorization)하는 API가 포함되어 있다.
상기 목록의 단일 호출 API는 authroization에 대하여 정의하지 않았으나,
향후 다음의 방안을 고려하여 추가할 예정이다.

- 1안) 인가앱 사업자가 서명한 `AttestedAppInfo` 정보를 확인한 후 일정기간 사용이 가능한 토큰을 발급
    - 단일 API 호출 시 헤더에 TAS 발행 토큰을 첨부
    - 별도의 토큰 관리 API 필요
- 2안) 인가앱 사업자가 인가앱에 토큰을 발행하고 TAS가 인가앱 사업자에 토큰 검증을 요청
    - 단일 API 호출 시 헤더에 인가앱 사업자 발행 토큰을 첨부
    - 인가앱 사업자가 토큰을 발행하고 검증해주는 기능 구현 필요

### 4.1. Issue Certificate VC
가입증명서 발급을 요청한다.

Verifier의 DID Document가 TAS 관리자를 통하여 저장소(예:블록체인)에 이미 등록되어 있어야 한다.
이 API에서는 TAS의 P120 프로토콜 API를 순서대로 호출하여 가입증명서를 발급 받는다.

| Item          | Description                     | Remarks |
| ------------- | ------------------------------- | ------- |
| Method        | `POST`                          |         |
| Path          | `/api/v1/certificate-vc` |         |
| Authorization | -                               |         |

#### 4.1.1. Request

**■ Path Parameters**

N/A

**■ Query Parameters**

N/A

**■ HTTP Body**

```c#
def object IssueCertificateVc: "Issue Certificate VC 요청문"
{    
}
```

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.1.2. Response

**■ Process**
1. TA P120 프로토콜의 API를 순서대로 호출
1. 발급받은 가입증명서를 DB에 저장

**■ Status 200 - Success**

```c#
def object _IssueCertificateVc: "Issue Certificate VC 응답문"
{    
}
```

**■ Status 400 - Client error**

N/A

**■ Status 500 - Server error**

| Code         | Description                                          |
| ------------ | ---------------------------------------------------- |
| SSRVTRAXXXXX | TAS_API를 참고해주세요                               |
| SSRVWLT00001 | 'issue-certificate-vc' API 요청 처리를 실패했습니다. |

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.1.3. Example

**■ Request**

```shell
curl -v -X POST "http://${Host}:${Port}/wallet/api/v1/certificate-vc" \
-H "Content-Type: application/json;charset=utf-8" \
-d @"data.json"
```

```json
//data.json
{
}
```

**■ Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
}
```

<div style="page-break-after: always; margin-top: 40px;"></div>

### 4.2. Get Certificate Vc

가입증명서를 조회한다.

| Item          | Description              | Remarks |
| ------------- | ------------------------ | ------- |
| Method        | `GET`                    |         |
| Path          | `/api/v1/certificate-vc` |         |
| Authorization | -                        |         |

#### 4.2.1. Request

**■ HTTP Headers**

| Header           | Value                            | Remarks |
| ---------------- | -------------------------------- | ------- |
| + `Content-Type` | `application/json;charset=utf-8` |         |     

**■ Path Parameters**

N/A

**■ Query Parameters**

N/A

**■ HTTP Body**

N/A

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.2.2. Response

**■ Process**
1. 가입증명서 조회

**■ Status 200 - Success**

```c#
def object _GetCertificateVc: "Get Certificate VC 응답문"
{
    @spread(Vc) // 데이터 명세서 참고
}
```

**■ Status 400 - Client error**


| Code         | Description                         |
| ------------ | ----------------------------------- |
| SSRVWLT00200 | Certifiacte VC가 존재하지 않습니다. |

**■ Status 500 - Server error**

| Code         | Description                                        |
| ------------ | -------------------------------------------------- |
| SSRVWLT00002 | 'get-certificate-vc' API 요청 처리를 실패했습니다. |

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.2.3. Example

**■ Request**

```shell
curl -v -X GET "http://${Host}:${Port}/wallet/api/v1/certificate-vc"
```

**■ Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
    작성필요
}
```

<div style="page-break-after: always; margin-top: 40px;"></div>

### 4.3. Request Sign Wallet

DID Document 서명을 요청한다.

Wallet과 같은 장치의 DID Document를 등록하려면 Wallet 사업자가 대신 서명해야 하므로, 이를 처리하기 위해 이 API가 필요하다.

| Item          | Description                          | Remarks |
| ------------- | ------------------------------------ | ------- |
| Method        | `POST`                               |         |
| Path          | `/wallet/api/v1/request-sign-wallet` |         |
| Authorization | -                                    |         |

#### 4.3.1. Request

**■ HTTP Headers**

| Header           | Value                            | Remarks |
| ---------------- | -------------------------------- | ------- |
| + `Content-Type` | `application/json;charset=utf-8` |         |     

**■ Path Parameters**

N/A

**■ Query Parameters**

N/A

**■ HTTP Body**

```c#
def object RequestSignWallet: "Request Sign Wallet 요청문"
{    
    @spread(DidDoc) // 데이터 명세서 참고
}
```

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.3.2. Response

**■ Process**
1. DID Document 키 서명 검증
1. walletId 생성
1. nonce 생성
1. AttestedDidDoc 생성
1. AttestedDidDoc 서명

**■ Status 200 - Success**

```c#
def object _RequestSignWallet: "Request Sign Wallet 응답문"
{
    @spread(AttestedDidDoc) // 데이터 명세서 참고
}
```

**■ Status 400 - Client error**


| Code         | Description                 |
| ------------ | --------------------------- |
| SSRVWLT00302 | Wallet 서명에 실패했습니다. |

**■ Status 500 - Server error**

| Code         | Description                                         |
| ------------ | --------------------------------------------------- |
| SSRVWLT00003 | 'request-sign-wallet' API 요청 처리를 실패했습니다. |

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.3.3. Example

**■ Request**

```shell
curl -v -X POST "http://${Host}:${Port}/wallet/api/v1/request-sign-wallet" \
-H "Content-Type: application/json;charset=utf-8" \
-d @"data.json"
```

```json
//data.json
{
  "id":"did:omn:2K8iHSTyZECsQx3FWUE53gBc6Xr5",
  "controller":"did:omn:tas",
  "created":"2024-09-05T08:16:57Z",
  "updated":"2024-09-05T08:16:57Z",
  "versionId":"1",
  "deactivated":false,
  "verificationMethod":[
    {
      "id":"keyagree",
      "type":"Secp256r1VerificationKey2018",
      "controller":"did:omn:tas",
      "publicKeyMultibase":"mA7RK+/86wDv...AdA+JUg+eT",
      "authType":1
    },
    {
      "id":"auth",
      "type":"Secp256r1VerificationKey2018",
      "controller":"did:omn:tas",
      "publicKeyMultibase":"mAqmLQlpWKJS...qZv9F49epZR",
      "authType":1
    },
    {
      "id":"assert",
      "type":"Secp256r1VerificationKey2018",
      "controller":"did:omn:tas",
      "publicKeyMultibase":"mAzVzjaLzpaMf...mwg4JqBZ9U",
      "authType":1
    }],
  "assertionMethod":[
    "assert"],
  "authentication":[
    "auth"],
  "keyAgreement":[
    "keyagree"],
  "proofs":[
    {
      "type":"Secp256r1Signature2018",
      "created":"2024-09-05T08:16:57Z",
      "verificationMethod":"did:omn:2K8iHSTyZECsQx3FWUE53gBc6Xr5?versionId=1#assert",
      "proofPurpose":"assertionMethod",
      "proofValue":"mIIRwTyyyX8...+1UXhQK/TDg="
    },
    {
      "type":"Secp256r1Signature2018",
      "created":"2024-09-05T08:16:57Z",
      "verificationMethod":"did:omn:2K8iHSTyZECsQx3FWUE53gBc6Xr5?versionId=1#auth",
      "proofPurpose":"authentication",
      "proofValue":"mIK1I...HFMLzn8gjqM2q8="
    }]
}

```

**■ Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
  "walletId":"WID202409bRzlIZtOmPM",
  "nonce":"9d47acc73663e9ab436c94d31be32b62",
  "ownerDidDoc":"ueyJAY29udGV4dCI6...idmVyc2lvbklkIjoiMSJ9",
  "provider":{
    "did":"did:omn:wallet",
    "certVcRef":"http://127.0.0.1:8095/wallet/api/v1/certificate-vc"
  },
  "did":"did:omn:wallet",
  "proof":{
    "type":"Secp256r1Signature2018",
    "created":"2024-09-05T17:16:56.769513Z",
    "verificationMethod":"did:omn:wallet?versionId=1#assert",
    "proofPurpose":"assertionMethod",
    "proofValue":"mH7lqBPWwzQ...ePdEv69TE"
  }
}

```
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

- Date: 2024-08-19
- Version: v1.0.0
  
Table of Contents
---
<!-- TOC tocDepth:2..3 chapterDepth:2..6 -->

- [1. Overview](#1-overview)
- [2. Terminology](#2-terminology)
- [3. API List](#3-api-list)
    - [3.1. Sequential API](#31-sequential-api)
    - [3.2. Single Call API](#32-single-call-api)
- [4. Single Call API](#4-single-call-api)
    - [4.1. Issue Certificate VC](#41-issue-certificate-vc)
    - [4.2. Get Certificate Vc](#42-get-certificate-vc)
    - [4.3. Request Sign Wallet](#43-request-sign-wallet)

<!-- /TOC -->


## 1. Overview

This document defines the APIs provided by the Wallet Service.

<div style="page-break-after: always; margin-top: 50px;"></div>

## 2. Terminology
- Protocol
  - A set of `Sequential APIs` that must be called in a predetermined order to perform a specific function. The API call sequence must be strictly followed, as incorrect ordering may lead to unexpected results.
- Sequential API
  - A series of APIs that are called in a predetermined order to perform a specific function (protocol). Each API must be called sequentially, and incorrect ordering may prevent proper operation.
  - However, some protocols may have APIs with the same calling sequence, in which case one API can be selected and called.
- Single Call API
  - An API that can be called independently regardless of order, like typical REST APIs.
- Standard API
  - APIs clearly defined in the API documentation that must be provided consistently across all implementations. Standard APIs ensure interoperability between systems and must operate according to predefined specifications.
- Non-Standard API
  - APIs that can be defined or customized differently according to the needs of each implementation. The non-standard APIs provided in this document are just one example and may be implemented differently for each implementation. In this case, separate documentation for each implementation is required.
  - For example, DID Document signing may be implemented differently depending on the system, and non-standard APIs like `request-sign-wallet` can be redefined as needed by each implementation.

<div style="page-break-after: always; margin-top: 50px;"></div>

## 3. API List

### 3.1. Sequential API
The Wallet Service currently has no protocols defined for performing specific functions, and therefore does not provide sequential APIs.

<div style="page-break-after: always; margin-top: 40px;"></div>

### 3.2. Single Call API

| API                    | URL                         | Description                | Standard API |
| ---------------------- | --------------------------- | -------------------------- | ------------ |
| `issue-certificate-vc` | /api/v1/certificate-vc      | Entity registration request| N            |
| `get-certificate-vc`   | /api/v1/certificate-vc      | Certificate inquiry        | N            |
| `request-sign-wallet`  | /api/v1/request-sign-wallet | DID Document sign request  | N            |

<div style="page-break-after: always; margin-top: 50px;"></div>

## 4. Single Call API

Single Call APIs are independent APIs that perform a specific function.
Therefore, they are not Sequential APIs (aka protocols) which are groups of APIs that must be called in order, so no protocol number is assigned.
The list of Single Call APIs provided by the Wallet Service is shown in the table below.

| API                    | URL                         | Description                | Standard API |
| ---------------------- | --------------------------- | -------------------------- | ------------ |
| `issue-certificate-vc` | /api/v1/certificate-vc      | Entity registration request| N            |
| `get-certificate-vc`   | /api/v1/certificate-vc      | Certificate inquiry        | N            |
| `request-sign-wallet`  | /api/v1/request-sign-wallet | DID Document sign request  | N            |

■ Authorization

Protocols include APIs that 'verify the caller's call permission' (authorization).
The Single Call APIs in the above list do not define authorization,
but the following approaches are being considered for future addition.

- Option 1) Issue a token that can be used for a certain period after verifying the `AttestedAppInfo` information signed by the authorized app provider
    - Attach TAS-issued token to header when calling single API
    - Separate token management API required
- Option 2) Authorized app provider issues token to authorized app and TAS requests token verification from authorized app provider
    - Attach authorized app provider-issued token to header when calling single API
    - Authorized app provider needs to implement functionality to issue and verify tokens

### 4.1. Issue Certificate VC
Request issuance of certificate.

The Verifier's DID Document must already be registered in storage (e.g., blockchain) through the TAS administrator.
This API sequentially calls TAS's P120 protocol APIs to obtain the certificate.

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
def object IssueCertificateVc: "Issue Certificate VC request"
{    
}
```

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.1.2. Response

**■ Process**
1. Call TA P120 protocol APIs in sequence
1. Save issued certificate to database

**■ Status 200 - Success**

```c#
def object _IssueCertificateVc: "Issue Certificate VC response"
{    
}
```

**■ Status 400 - Client error**

N/A

**■ Status 500 - Server error**

| Code         | Description                                          |
| ------------ | ---------------------------------------------------- |
| SSRVTRAXXXXX | Please refer to TAS_API                             |
| SSRVWLT00001 | Failed to process 'issue-certificate-vc' API request. |

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

Retrieve certificate.

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
1. Retrieve certificate

**■ Status 200 - Success**

```c#
def object _GetCertificateVc: "Get Certificate VC response"
{
    @spread(Vc) // Refer to data specification
}
```

**■ Status 400 - Client error**


| Code         | Description                         |
| ------------ | ----------------------------------- |
| SSRVWLT00200 | Certificate VC does not exist. |

**■ Status 500 - Server error**

| Code         | Description                                        |
| ------------ | -------------------------------------------------- |
| SSRVWLT00002 | Failed to process 'get-certificate-vc' API request. |

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
    To be written
}
```

<div style="page-break-after: always; margin-top: 40px;"></div>

### 4.3. Request Sign Wallet

Request DID Document signing.

To register a DID Document for devices like Wallet, the Wallet provider must sign on behalf, so this API is needed to handle this process.

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
def object RequestSignWallet: "Request Sign Wallet request"
{    
    @spread(DidDoc) // Refer to data specification
}
```

<div style="page-break-after: always; margin-top: 30px;"></div>

#### 4.3.2. Response

**■ Process**
1. Verify DID Document key signature
1. Generate walletId
1. Generate nonce
1. Create AttestedDidDoc
1. Sign AttestedDidDoc

**■ Status 200 - Success**

```c#
def object _RequestSignWallet: "Request Sign Wallet response"
{
    @spread(AttestedDidDoc) // Refer to data specification
}
```

**■ Status 400 - Client error**


| Code         | Description                 |
| ------------ | --------------------------- |
| SSRVWLT00302 | Failed to sign wallet. |

**■ Status 500 - Server error**

| Code         | Description                                         |
| ------------ | --------------------------------------------------- |
| SSRVWLT00003 | Failed to process 'request-sign-wallet' API request. |

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
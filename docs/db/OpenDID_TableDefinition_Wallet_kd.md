
Open DID Wallet 데이터베이스 테이블 정의
==

- Date: 2025-03-31
- Version: v1.0.1 (dev)

목차
--
- [Open DID Wallet 데이터베이스 테이블 정의](#open-did-wallet--데이터베이스-테이블-정의)
  - [목차](#목차)
  - [1. 개요](#1-개요)
    - [1.1 ERD](#11-erd)
  * [2. 테이블 정의](#2-테이블-정의)
    - [2.1 Wallet](#21-wallet)
    - [2.2 Certificate VC](#22-certificate-vc)
    - [2.3 Wallet Service](#23-wallet-service)
    - [2.4. Admin](#24-admin)

## 1. 개요

이 문서는 Wallet 서버에서 사용하는 데이터베이스 테이블의 구조를 정의합니다. 각 테이블의 필드 속성, 관계, 데이터 흐름을 설명하며, 시스템 개발 및 유지보수의 필수 참고 자료로 활용됩니다.

### 1.1 ERD

[ERD](https://www.erdcloud.com/d/KBdJJZYMbJLuWAWPD) 사이트에 접속하여 다이어그램을 확인하세요. 이 다이어그램은 CA 서버 데이터베이스의 테이블 간 관계, 주요 속성, 기본 키 및 외래 키 관계를 시각적으로 보여줍니다.

## 2. 테이블 정의

### 2.1 Wallet

| Key  | Column Name | Data Type | Length | Nullable | Default | Description        |
|------|-------------|-----------|--------|----------|---------|--------------------|
| PK   | id          | BIGINT    |        | NO       | N/A     | id                 |
|      | wallet_did  | VARCHAR   | 200    | NO       | N/A     | wallet DID         |
|      | wallet_id   | VARCHAR   | 200    | NO       | N/A     | wallet ID          |
|      | created_at  | TIMESTAMP |        | NO       | NOW()   | created date       |
|      | updated_at  | TIMESTAMP |        | YES      | N/A     | updated date       |

### 2.2 Certificate VC

| Key  | Column Name | Data Type | Length | Nullable | Default | Description        |
|------|-------------|-----------|--------|----------|---------|--------------------|
| PK   | id          | BIGINT    |        | NO       | N/A     | id                 |
|      | vc          | TEXT      |        | NO       | N/A     | vc                 |
|      | created_at  | TIMESTAMP |        | NO       | NOW()   | created date       |
|      | updated_at  | TIMESTAMP |        | YES      | N/A     | updated date       |

### 2.3 Wallet Service

| Key | Column Name    | Data Type | Length | Nullable | Default | Description           |
| --- | -------------- | --------- | ------ | -------- | ------- | --------------------- |
| PK  | id             | BIGINT    |        | NO       | N/A     | id                    |
|     | did            | VARCHAR   | 200    | NO       | N/A     | wallet service DID    |
|     | name           | VARCHAR   | 200    | NO       | N/A     | wallet service name   |
|     | status         | VARCHAR   | 50     | NO       | N/A     | wallet service status |
|     | server_url     | VARCHAR   | 2000   | YES      | N/A     | wallet service URL    |
|     | certificate_ur | VARCHAR   | 2000   | YES      | N/A     | certificate URL       |
|     | created_at     | TIMESTAMP |        | NO       | now()   | created date          |
|     | updated_at     | TIMESTAMP |        | YES      | N/A     | updated date          |

### 2.4. Admin

This table stores information about administrator accounts.

| Key | Column Name            | Data Type | Length | Nullable | Default | Description                        |
| --- | ---------------------- | --------- | ------ | -------- | ------- | ---------------------------------- |
| PK  | id                     | BIGINT    |        | NO       | N/A     | id                                 |
|     | login_id               | VARCHAR   | 50     | NO       | N/A     | login ID                           |
|     | login_password         | VARCHAR   | 64     | NO       | N/A     | hashed login password              |
|     | name                   | VARCHAR   | 200    | YES      | N/A     | administrator name                 |
|     | email                  | VARCHAR   | 100    | YES      | N/A     | email address                      |
|     | email_verified         | BOOLEAN   |        | YES      | false   | whether email is verified          |
|     | require_password_reset | BOOLEAN   |        | NO       | true    | whether password reset is required |
|     | role                   | VARCHAR   | 50     | NO       | N/A     | administrator role                 |
|     | created_by             | VARCHAR   | 50     | NO       | N/A     | creator's login ID                 |
|     | created_at             | TIMESTAMP |        | NO       | now()   | created date                       |
|     | updated_at             | TIMESTAMP |        | YES      | N/A     | updated date                       |

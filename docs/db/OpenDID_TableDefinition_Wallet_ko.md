
Open DID Wallet 데이터베이스 테이블 정의
==

- Date: 2026-11-03
- Version: v3.0.0

| 버전 | 일자 | 변경 내용 |
| ---- | ---- | --------- |
| v1.0.1 | 2025-03-31 | 최초 작성(dev) |
| v3.0.0 | 2026-11-03 | 서버 설정·비밀번호 정책·API 로그·DID Document 테이블 추가, 컬럼 정의를 현재 스키마에 맞춤 |

목차
--
- [Open DID Wallet 데이터베이스 테이블 정의](#open-did-wallet--데이터베이스-테이블-정의)
  - [목차](#목차)
  - [1. 개요](#1-개요)
    - [1.1 ERD](#11-erd)
  * [2. 테이블 정의](#2-테이블-정의)
    - [2.1. Wallet](#21-wallet)
    - [2.2. Certificate VC](#22-certificate-vc)
    - [2.3. Wallet Service](#23-wallet-service)
    - [2.4. Admin](#24-admin)
    - [2.5. DID Document](#25-did-document)
    - [2.6. Server Config](#26-server-config)
    - [2.7. Admin Password Policy](#27-admin-password-policy)
    - [2.8. API Log](#28-api-log)

## 1. 개요

이 문서는 Wallet 서버에서 사용하는 데이터베이스 테이블의 구조를 정의합니다. 각 테이블의 필드 속성, 관계, 데이터 흐름을 설명하며, 시스템 개발 및 유지보수의 필수 참고 자료로 활용됩니다.

### 1.1 ERD

[ERD](https://www.erdcloud.com/d/KBdJJZYMbJLuWAWPD) 사이트에 접속하여 다이어그램을 확인하세요. 이 다이어그램은 CA 서버 데이터베이스의 테이블 간 관계, 주요 속성, 기본 키 및 외래 키 관계를 시각적으로 보여줍니다.

## 2. 테이블 정의

### 2.1. Wallet

Wallet Service에 등록된 지갑(지갑 DID, 지갑 아이디)을 저장하는 테이블입니다.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | 아이디 |
|  | wallet_did | VARCHAR | 200 | NO | N/A | 지갑 DID |
|  | wallet_id | VARCHAR | 200 | NO | N/A | 지갑 아이디 |
|  | created_at | TIMESTAMP |  | NO | now() | 생성 일시 |
|  | updated_at | TIMESTAMP |  | YES | N/A | 수정 일시 |

### 2.2. Certificate VC

Certificate VC(Verifiable Credential) 정보를 저장하는 테이블입니다.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | 아이디 |
|  | vc | TEXT |  | NO | N/A | VC |
|  | created_at | TIMESTAMP |  | NO | now() | 생성 일시 |
|  | updated_at | TIMESTAMP |  | YES | N/A | 수정 일시 |

### 2.3. Wallet Service

Wallet Service 정보를 저장하는 테이블입니다.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | 아이디 |
|  | did | VARCHAR | 200 | YES | N/A | Wallet Service DID |
|  | name | VARCHAR | 200 | NO | N/A | Wallet Service 이름 |
|  | status | VARCHAR | 50 | NO | N/A | Wallet Service 상태 |
|  | server_url | VARCHAR | 2000 | YES | N/A | Wallet Service URL |
|  | certificate_url | VARCHAR | 2000 | YES | N/A | 가입증명서 URL |
|  | created_at | TIMESTAMP |  | NO | now() | 생성 일시 |
|  | updated_at | TIMESTAMP |  | YES | N/A | 수정 일시 |
|  | published_certificate_url | VARCHAR | 2000 | YES | N/A | List Provider에 게시된 가입증명서 VC URL |

### 2.4. Admin

관리자 계정 정보를 저장하는 테이블입니다.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | 아이디 |
|  | login_id | VARCHAR | 50 | NO | N/A | 로그인 아이디 |
|  | login_password | VARCHAR | 64 | NO | N/A | 해시된 로그인 비밀번호 |
|  | name | VARCHAR | 200 | YES | N/A | 관리자 이름 |
|  | email_verified | BOOLEAN |  | NO | false | 이메일 인증 여부 |
|  | require_password_reset | BOOLEAN |  | NO | true | 비밀번호 재설정 필요 여부 |
|  | role | VARCHAR | 50 | NO | N/A | 관리자 역할 |
|  | created_by | VARCHAR | 50 | NO | N/A | 생성자 로그인 아이디 |
|  | created_at | TIMESTAMP |  | NO | now() | 생성 일시 |
|  | updated_at | TIMESTAMP |  | YES | N/A | 수정 일시 |
|  | last_password_changed_at | TIMESTAMP |  | YES | N/A | 마지막 비밀번호 변경 일시 |
|  | password_reset_reason | VARCHAR | 20 | YES | N/A | 비밀번호 재설정이 필요한 사유 |

### 2.5. DID Document

Wallet Service의 DID Document를 저장하는 테이블입니다.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | 아이디 |
|  | did_document | TEXT |  | NO | N/A | DID Document(JSON) |
|  | created_at | TIMESTAMP |  | NO | now() | 생성 일시 |
|  | updated_at | TIMESTAMP |  | YES | N/A | 수정 일시 |

### 2.6. Server Config

LSS API Key 등 외부 서비스 연동 설정을 저장하는 테이블입니다.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | 아이디 |
|  | config_key | VARCHAR | 100 | NO | N/A | 설정 키(예: API_KEY_LSS) |
|  | config_value | TEXT |  | NO | N/A | 설정 값 |
|  | description | VARCHAR | 200 | YES | N/A | 설명 |
|  | created_at | TIMESTAMP |  | NO | now() | 생성 일시 |
|  | updated_at | TIMESTAMP |  | YES | N/A | 수정 일시 |

### 2.7. Admin Password Policy

관리자 계정의 비밀번호 정책을 저장하는 테이블입니다.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | 아이디 |
|  | min_length | SMALLINT |  | NO | N/A | 비밀번호 최소 길이 |
|  | require_uppercase | BOOLEAN |  | NO | false | 대문자 포함 필수 여부 |
|  | require_number | BOOLEAN |  | NO | false | 숫자 포함 필수 여부 |
|  | require_special | BOOLEAN |  | NO | false | 특수문자 포함 필수 여부 |
|  | password_expiry_days | SMALLINT |  | NO | N/A | 비밀번호 만료 기간(일) |
|  | created_at | TIMESTAMP |  | NO | now() | 생성 일시 |
|  | updated_at | TIMESTAMP |  | YES | N/A | 수정 일시 |

### 2.8. API Log

API 호출 로그와 감사 로그를 저장하는 테이블입니다.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | 아이디 |
|  | method | VARCHAR | 10 | NO | N/A | HTTP 메서드 |
|  | uri | VARCHAR | 1048 | NO | N/A | 요청 URI |
|  | status | INTEGER |  | NO | N/A | HTTP 상태 코드 |
|  | result | VARCHAR | 20 | NO | N/A | 처리 결과(성공·실패) |
|  | api_name | VARCHAR | 255 | YES | N/A | API 이름 |
|  | api_description | VARCHAR | 1000 | YES | N/A | API 설명 |
|  | requester_id | VARCHAR | 128 | YES | N/A | 요청자 아이디 |
|  | duration_ms | BIGINT |  | NO | N/A | 처리 시간(ms) |
|  | client_ip | VARCHAR | 64 | YES | N/A | 클라이언트 IP |
|  | error_code | VARCHAR | 64 | YES | N/A | 에러 코드 |
|  | message | TEXT |  | YES | N/A | 메시지 |
|  | api_type | VARCHAR | 20 | YES | N/A | API 유형(AGENT, ADMIN) |
|  | created_at | TIMESTAMP |  | NO | N/A | 생성 일시 |
|  | updated_at | TIMESTAMP |  | YES | N/A | 수정 일시 |
|  | target_type | VARCHAR | 50 | YES | N/A | 감사 대상 유형 |
|  | target_id | VARCHAR | 128 | YES | N/A | 감사 대상 아이디 |
|  | user_agent | TEXT |  | YES | N/A | User-Agent 헤더 |
|  | action_type | VARCHAR | 20 | NO | N/A | 작업 유형(예: CREATE, UPDATE, DELETE) |

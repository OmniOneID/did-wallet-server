
Open DID Wallet Database Table Definition
==

- Date: 2026-11-03
- Version: v3.0.0

| Version | Date | Changes |
| ------- | ---- | ------- |
| v1.0.1 | 2025-03-31 | Initial version (dev) |
| v3.0.0 | 2026-11-03 | Added server configuration, password policy, API log, and DID Document tables; aligned column definitions with the current schema |

Contents
--
- [Open DID Wallet Database Table Definition](#open-did-wallet-database-table-definition)
  - [Contents](#contents)
  - [1. Overview](#1-overview)
    - [1.1 ERD](#11-erd)
  - [2. Table Definition](#2-table-definition)
    - [2.1. Wallet](#21-wallet)
    - [2.2. Certificate VC](#22-certificate-vc)
    - [2.3. Wallet Service](#23-wallet-service)
    - [2.4. Admin](#24-admin)
    - [2.5. DID Document](#25-did-document)
    - [2.6. Server Config](#26-server-config)
    - [2.7. Admin Password Policy](#27-admin-password-policy)
    - [2.8. API Log](#28-api-log)

## 1. Overview

This document defines the structure of the database tables used in the Wallet server. It describes the field attributes, relationships, and data flow for each table, serving as essential reference material for system development and maintenance.

### 1.1 ERD

Access the [ERD](https://www.erdcloud.com/d/KBdJJZYMbJLuWAWPD) site to view the diagram, which visually represents the relationships between the tables in the Wallet server database, including key attributes, primary keys, and foreign key relationships.

## 2. Table Definition

### 2.1. Wallet

This table stores wallets registered with the Wallet service (wallet DID and wallet ID).

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | id |
|  | wallet_did | VARCHAR | 200 | NO | N/A | wallet DID |
|  | wallet_id | VARCHAR | 200 | NO | N/A | wallet ID |
|  | created_at | TIMESTAMP |  | NO | now() | created date |
|  | updated_at | TIMESTAMP |  | YES | N/A | updated date |

### 2.2. Certificate VC

This table stores Certificate VC (Verifiable Credential) information.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | id |
|  | vc | TEXT |  | NO | N/A | vc |
|  | created_at | TIMESTAMP |  | NO | now() | created date |
|  | updated_at | TIMESTAMP |  | YES | N/A | updated date |

### 2.3. Wallet Service

This table stores information about the Wallet service.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | id |
|  | did | VARCHAR | 200 | YES | N/A | wallet service DID |
|  | name | VARCHAR | 200 | NO | N/A | wallet service name |
|  | status | VARCHAR | 50 | NO | N/A | wallet service status |
|  | server_url | VARCHAR | 2000 | YES | N/A | wallet service URL |
|  | certificate_url | VARCHAR | 2000 | YES | N/A | certificate URL |
|  | created_at | TIMESTAMP |  | NO | now() | created date |
|  | updated_at | TIMESTAMP |  | YES | N/A | updated date |
|  | published_certificate_url | VARCHAR | 2000 | YES | N/A | URL of the certificate VC published to the List Provider |

### 2.4. Admin

This table stores information about administrator accounts.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | id |
|  | login_id | VARCHAR | 50 | NO | N/A | login ID |
|  | login_password | VARCHAR | 64 | NO | N/A | hashed login password |
|  | name | VARCHAR | 200 | YES | N/A | administrator name |
|  | email_verified | BOOLEAN |  | NO | false | whether email is verified |
|  | require_password_reset | BOOLEAN |  | NO | true | whether password reset is required |
|  | role | VARCHAR | 50 | NO | N/A | administrator role |
|  | created_by | VARCHAR | 50 | NO | N/A | creator's login ID |
|  | created_at | TIMESTAMP |  | NO | now() | created date |
|  | updated_at | TIMESTAMP |  | YES | N/A | updated date |
|  | last_password_changed_at | TIMESTAMP |  | YES | N/A | last password change date |
|  | password_reset_reason | VARCHAR | 20 | YES | N/A | reason a password reset is required |

### 2.5. DID Document

This table stores the DID Document of the Wallet service.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | id |
|  | did_document | TEXT |  | NO | N/A | DID Document (JSON) |
|  | created_at | TIMESTAMP |  | NO | now() | created date |
|  | updated_at | TIMESTAMP |  | YES | N/A | updated date |

### 2.6. Server Config

This table stores settings for integration with external services, such as the LSS API key.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | id |
|  | config_key | VARCHAR | 100 | NO | N/A | configuration key (e.g. API_KEY_LSS) |
|  | config_value | TEXT |  | NO | N/A | configuration value |
|  | description | VARCHAR | 200 | YES | N/A | description |
|  | created_at | TIMESTAMP |  | NO | now() | created date |
|  | updated_at | TIMESTAMP |  | YES | N/A | updated date |

### 2.7. Admin Password Policy

This table stores the password policy for administrator accounts.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | id |
|  | min_length | SMALLINT |  | NO | N/A | minimum password length |
|  | require_uppercase | BOOLEAN |  | NO | false | whether an uppercase letter is required |
|  | require_number | BOOLEAN |  | NO | false | whether a number is required |
|  | require_special | BOOLEAN |  | NO | false | whether a special character is required |
|  | password_expiry_days | SMALLINT |  | NO | N/A | password expiry period (days) |
|  | created_at | TIMESTAMP |  | NO | now() | created date |
|  | updated_at | TIMESTAMP |  | YES | N/A | updated date |

### 2.8. API Log

This table stores API call logs and audit logs.

| Key | Column Name | Data Type | Length | Nullable | Default | Description |
|-----|-------------|-----------|--------|----------|---------|-------------|
| PK | id | BIGINT |  | NO | N/A | id |
|  | method | VARCHAR | 10 | NO | N/A | HTTP method |
|  | uri | VARCHAR | 1048 | NO | N/A | request URI |
|  | status | INTEGER |  | NO | N/A | HTTP status code |
|  | result | VARCHAR | 20 | NO | N/A | result (success or failure) |
|  | api_name | VARCHAR | 255 | YES | N/A | API name |
|  | api_description | VARCHAR | 1000 | YES | N/A | API description |
|  | requester_id | VARCHAR | 128 | YES | N/A | requester ID |
|  | duration_ms | BIGINT |  | NO | N/A | processing time (ms) |
|  | client_ip | VARCHAR | 64 | YES | N/A | client IP |
|  | error_code | VARCHAR | 64 | YES | N/A | error code |
|  | message | TEXT |  | YES | N/A | message |
|  | api_type | VARCHAR | 20 | YES | N/A | API type (AGENT, ADMIN) |
|  | created_at | TIMESTAMP |  | NO | N/A | created date |
|  | updated_at | TIMESTAMP |  | YES | N/A | updated date |
|  | target_type | VARCHAR | 50 | YES | N/A | audit target type |
|  | target_id | VARCHAR | 128 | YES | N/A | audit target ID |
|  | user_agent | TEXT |  | YES | N/A | User-Agent header |
|  | action_type | VARCHAR | 20 | NO | N/A | action type (e.g. CREATE, UPDATE, DELETE) |

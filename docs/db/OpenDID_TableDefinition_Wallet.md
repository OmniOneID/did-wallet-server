
Open DID Wallet Database Table Definition
==

- Date: 2025-03-31
- Version: v1.0.1 (dev)

Contents
--
- [Open DID Wallet Database Table Definition](#open-did-wallet-database-table-definition)
  - [Contents](#contents)
  - [1. Overview](#1-overview)
    - [1.1 ERD](#11-erd)
  - [2. Table Definition](#2-table-definition)
    - [2.1 Wallet](#21-wallet)
    - [2.2 Certificate VC](#22-certificate-vc)
    - [2.3 Wallet Service](#23-wallet-service)
    - [2.4. Admin](#24-admin)

## 1. Overview

This document defines the structure of the database tables used in the Wallet server. It describes the field attributes, relationships, and data flow for each table, serving as essential reference material for system development and maintenance.

### 1.1 ERD

Access the [ERD](https://www.erdcloud.com/d/KBdJJZYMbJLuWAWPD) site to view the diagram, which visually represents the relationships between the tables in the Wallet server database, including key attributes, primary keys, and foreign key relationships.

## 2. Table Definition

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

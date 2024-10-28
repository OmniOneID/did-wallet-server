
Open DID Wallet Database Table Definition
==

- Date: 2024-09-04
- Version: v1.0.0

Contents
--
- [Open DID Wallet Database Table Definition](#open-did-wallet-database-table-definition)
  - [Contents](#contents)
  - [1. Overview](#1-overview)
  - [2. Table Definition](#2-table-definition)
    - [2.1 WALLET](#21-wallet)
    - [2.2 CERTIFICATE VC](#22-certificate-vc)

## 1. Overview

This document defines the structure of the database tables used in the Wallet server. It describes the field attributes, relationships, and data flow for each table, serving as essential reference material for system development and maintenance.

### 1.1 ERD

Access the [ERD](https://www.erdcloud.com/d/A4igYToBGfHTnNfvA) site to view the diagram, which visually represents the relationships between the tables in the Wallet server database, including key attributes, primary keys, and foreign key relationships.

## 2. Table Definition

### 2.1 WALLET

| Key  | Column Name | Data Type | Length | Nullable | Default | Description        |
|------|-------------|-----------|--------|----------|---------|--------------------|
| PK   | id          | BIGINT    |        | NO       | N/A     | id                 |
|      | wallet_did  | VARCHAR   | 200    | NO       | N/A     | wallet DID         |
|      | wallet_id   | VARCHAR   | 200    | NO       | N/A     | wallet ID          |
|      | created_at  | TIMESTAMP |        | NO       | NOW()   | created date       |
|      | updated_at  | TIMESTAMP |        | YES      | N/A     | updated date       |

### 2.2 CERTIFICATE VC

| Key  | Column Name | Data Type | Length | Nullable | Default | Description        |
|------|-------------|-----------|--------|----------|---------|--------------------|
| PK   | id          | BIGINT    |        | NO       | N/A     | id                 |
|      | vc          | TEXT      |        | NO       | N/A     | vc                 |
|      | created_at  | TIMESTAMP |        | NO       | NOW()   | created date       |
|      | updated_at  | TIMESTAMP |        | YES      | N/A     | updated date       |
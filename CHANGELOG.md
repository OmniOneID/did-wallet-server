# Changelog

## [Unreleased] (v3.0.0)

### 🚀 New Features
- Stores the certificate VC URL published to the List Provider (TA enrollment response)
- LSS API key authentication (`lss` profile)
- Admin Console
    - Server Configuration (LSS API key)
    - Administrator password policy, admin ID change, and session management
    - Certificate VC view on the server management page
    - Search and refresh on list pages
    - API call logs and audit logs

### 🔧 Changes
- Replaced local utilities with did-common-sdk-server 3.0.0
- Upgraded Node.js to 22.23.4 for the admin console build
- Updated the Dockerfile Java image to eclipse-temurin 21 (jammy)
- Expanded the admin guide with item tables and example screens aligned with the admin console
- Server version 3.0.0 (`did-wallet-server-3.0.0.jar`)

### 🐛 Bug Fixes
- `INVALID_DID_DOCUMENT` used the same code as `TR_GET_CERTIFICATE_VC_FAILED` (SSRVWLT00102); it is now SSRVWLT00902
- Fixed a typo in the SSRVWLT00811 error message

## [Released] (v2.0.0)

## v2.0.0 (2025-05-30)

### 🚀 New Features
- Support for Besu blockchain


## [Unreleased] (v1.0.1)

### 🚀 New Features
- Admin Console
    - Wallet Service Management
    - Wallet Management
    - Admin Management
- Removed hardcoded logic and sample data from Wallet Server through Admin Console
  - Removed hardcoded YAML configuration values
  - Removed sample data previously embedded in Java source code
  
## v1.0.0 (2024-10-18)

### 🚀 New Features
- Wallet Server
    - Sign Wallet DID Document

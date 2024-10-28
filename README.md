Wallet Server
==

Welcome to the Wallet Server Repository. <br>
This repository contains the source code, documentation, and related resources for the Wallet Server.

## Folder Structure
Overview of the major folders and documents in the project directory:

```
did-wallet-server
├── CHANGELOG.md
├── CLA.md
├── CODE_OF_CONDUCT.md
├── CONTRIBUTING.md
├── LICENSE
├── dependencies-license.md
├── MAINTAINERS.md
├── README.md
├── RELEASE-PROCESS.md
├── SECURITY.md
├── docs
│   └── api
│       └── Wallet_API.md
│   └── db
│       └── OpenDID_TableDefinition_Wallet.md
│   └── errorCode
│       └── Wallet_ErrorCode.md
│   └── installation
│       └── OpenDID_WalletServer_InstallationAndOperation_Guide.md
└── source
    └── wallet
        ├── gradle
        ├── libs
            └── did-sdk-common-1.0.0.jar
            └── did-blockchain-sdk-server-1.0.0.jar
            └── did-core-sdk-server-1.0.0..jar
            └── did-crypto-sdk-server-1.0.0.jar
            └── did-datamodel-sdk-server-1.0.0.jar
            └── did-wallet-sdk-server-1.0.0.jar
        ├── sample
        └── src
        └── build.gradle
        └── README.md
```

<br/>

Below is a description of each folder and file in the directory:

| Name                    | Description                                     |
| ----------------------- | ----------------------------------------------- |
| CHANGELOG.md            | Version-specific changes in the project         |
| CODE_OF_CONDUCT.md      | Code of conduct for contributors                |
| CONTRIBUTING.md         | Contribution guidelines and procedures          |
| LICENSE                 | License                                         |
| dependencies-license.md | Licenses for the project’s dependency libraries |
| MAINTAINERS.md          | Guidelines for project maintainers              |
| RELEASE-PROCESS.md      | Procedures for releasing new versions           |
| SECURITY.md             | Security policies and vulnerability reporting   |
| docs                    | Documentation                                   |
| ┖ api                   | API guide documentation                         |
| ┖ db                    | Table Specifications                            |
| ┖ errorCode             | Error codes and troubleshooting guides          |
| ┖ installation          | Installation and setup instructions             |
| source                  | Server source code project                      |
| ┖ wallet                | Wallet Server source code and build files       |
|   ┖ gradle              | Gradle build configurations and scripts         |
|   ┖ libs                | External libraries and dependencies             |
|   ┖ sample              | Sample files                                    |
|   ┖ src                 | Main source code directory                      |
|   ┖ build.gradle        | Gradle build configuration file                 |
|   ┖ README.md           | Overview and instructions for the source code   |

<br/>


## Libraries

Libraries used in this project are organized into two main categories:

1. **Open DID Libraries**: These libraries are developed by the Open DID project and are available in the [libs folder](source/wallet/libs). They include:

   - `did-sdk-common-1.0.0.jar`
   - `did-blockchain-sdk-server-1.0.0.jar`
   - `did-core-sdk-server-1.0.0.jar`
   - `did-crypto-sdk-server-1.0.0.jar`
   - `did-datamodel-sdk-server-1.0.0.jar`
   - `did-wallet-sdk-server-1.0.0.jar`

2. **Third-Party Libraries**: These libraries are open-source dependencies managed via the [build.gradle](source/wallet/build.gradle) file. For a detailed list of third-party libraries and their licenses, please refer to the [dependencies-license.md](dependencies-license.md) file.

## Installation And Operation Guide

For detailed instructions on installing and configuring the Wallet Server, please refer to the guide below:
- [OpenDID Wallet Server Installation and Operation Guide](docs/installation/OpenDID_WalletServer_InstallationAndOperation_Guide.md)  

## API Reference

API documentation is available in two main types:

- **Wallet API**: Detailed reference for the Wallet Server's API endpoints and usage..
  - [Wallet API](docs/api/Wallet_API_ko.md)

## Change Log

The Change Log provides a detailed record of version-specific changes and updates. You can find it here:
- [Change Log](./CHANGELOG.md)  

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) and [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for details on our code of conduct, and the process for submitting pull requests to us.

## License
[Apache 2.0](LICENSE)
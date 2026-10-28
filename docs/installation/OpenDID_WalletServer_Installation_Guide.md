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

Open DID Wallet Server Installation And Operation Guide
==

- Date: 2025-05-29
- Version: v2.0.0

Table of Contents
==
- [Open DID Wallet Server Installation And Operation Guide](#open-did-wallet-server-installation-and-operation-guide)
- [Table of Contents](#table-of-contents)
- [1. Introduction](#1-introduction)
  - [1.1. Overview](#11-overview)
  - [1.2. Wallet Server Definition](#12-wallet-server-definition)
  - [1.3. System Requirements](#13-system-requirements)
- [2. Preparation](#2-preparation)
  - [2.1. Git Installation](#21-git-installation)
  - [2.2. PostgreSQL Installation](#22-postgresql-installation)
- [3. Cloning the Source Code from GitHub](#3-cloning-the-source-code-from-github)
  - [3.1. Cloning the Source Code](#31-cloning-the-source-code)
  - [3.2. Directory Structure](#32-directory-structure)
- [4. How to Run the Server](#4-how-to-run-the-server)
  - [4.1. Running with IntelliJ IDEA (Gradle Support)](#41-running-with-intellij-idea-gradle-support)
    - [4.1.1. Installing and Setting Up IntelliJ IDEA](#411-installing-and-setting-up-intellij-idea)
    - [4.1.2. Opening the Project in IntelliJ](#412-opening-the-project-in-intellij)
    - [4.1.3. Gradle Build](#413-gradle-build)
    - [4.1.4. Running the Server](#414-running-the-server)
    - [4.1.5. Database Installation](#415-database-installation)
    - [4.1.6. Server Configuration](#416-server-configuration)
  - [4.2. Running the Server Using Console Commands](#42-running-the-server-using-console-commands)
    - [4.2.1. Gradle Build Command](#421-gradle-build-command)
    - [4.2.2. Running the Server](#422-running-the-server)
    - [4.2.3. Database Installation](#423-database-installation)
    - [4.2.4. Server Configuration](#424-server-configuration)
  - [4.3. Running with Docker](#43-running-with-docker)
- [5. Configuration Guide.](#5-configuration-guide)
  - [5.1. application.yml](#51-applicationyml)
    - [5.1.1. Spring Basic Settings](#511-spring-basic-settings)
    - [5.1.2. Jackson Basic Settings](#512-jackson-basic-settings)
    - [5.1.3. Server Settings](#513-server-settings)
    - [5.1.4. TAS Settings](#514-tas-settings)
  - [5.2. application-logging.yml](#52-application-loggingyml)
    - [5.2.1. Logging Configuration](#521-logging-configuration)
  - [5.3. database.yml](#53-databaseyml)
    - [5.3.1. Spring Liquibase Settings](#531-spring-liquibase-settings)
    - [5.3.2. DataSource Settings](#532-datasource-settings)
    - [5.3.3. JPA Settings](#533-jpa-settings)
  - [5.4. wallet.yml](#54-walletyml)
    - [5.4.1. Wallet Access Information Configuration](#541-wallet-access-information-configuration)
  - [5.5. blockchain.properties](#55-blockchainproperties)
    - [5.5.1. Blockchain Integration Configuration](#551-blockchain-integration-configuration)
      - [EVM Network Configuration](#evm-network-configuration)
      - [EVM Contract Configuration](#evm-contract-configuration)
- [6. Profile Configuration and Usage](#6-profile-configuration-and-usage)
  - [6.1. Profile Overview (`sample`, `dev`)](#61-profile-overview-sample-dev)
    - [6.1.1. `sample` Profile](#611-sample-profile)
    - [6.1.2. `dev` Profile](#612-dev-profile)
  - [6.2. How to Configure Profiles](#62-how-to-configure-profiles)
    - [6.2.1. Running the Server Using an IDE](#621-running-the-server-using-an-ide)
    - [6.2.2. Running the Server Using Console Commands](#622-running-the-server-using-console-commands)
    - [6.2.3. Running the Server Using Docker](#623-running-the-server-using-docker)
- [7. Running After Building with Docker](#7-running-after-building-with-docker)
  - [7.1. Docker Image Build Method (Based on `Dockerfile`)](#71-docker-image-build-method-based-on-dockerfile)
    - [7.1.1. Build Docker image](#711-build-docker-image)
  - [7.2. Running with Docker Compose](#72-running-with-docker-compose)
    - [7.2.1. Preparing Directories and Configuration Files](#721-preparing-directories-and-configuration-files)
      - [1. Create docker-compose directory and config directory](#1-create-docker-compose-directory-and-config-directory)
      - [2. Copy configuration files (yml) to config directory](#2-copy-configuration-files-yml-to-config-directory)
      - [3. Modify blockchain.properties file](#3-modify-blockchainproperties-file)
      - [4. Modify application-database.yml file](#4-modify-application-databaseyml-file)
    - [7.2.2. Create `docker-compose.yml` file](#722-create-docker-composeyml-file)
    - [7.2.3. Run Container](#723-run-container)
- [8. Installing PostgreSQL with Docker](#8-installing-postgresql-with-docker)
  - [8.1. Installing PostgreSQL Using Docker Compose](#81-installing-postgresql-using-docker-compose)
  - [8.2. Running the PostgreSQL Container](#82-running-the-postgresql-container)
    

# 1. Introduction

## 1.1. Overview
This document provides a guide for the installation and operation of the Wallet server. It explains the Wallet server installation process, configuration methods, and operation procedures step-by-step, helping users install and manage the server efficiently.

- For the complete installation guide of OpenDID, please refer to the [Open DID Installation Guide].
- For the Admin console guide, please refer to [Open DID Admin Console Guide]

<br/>

## 1.2. Wallet Server Definition

The Wallet server provides the Device Key Attestation signature API functionality for Wallets in Open DID.

<br/>

## 1.3. System Requirements
- **Java 21** or higher
- **Gradle 7.0** or higher
- **Docker** and **Docker Compose** (when using Docker)
- At least **2GB RAM** and **10GB of disk space**

<br/>

# 2. Preparation

This chapter provides the necessary preparatory steps before installing the components of the Open DID project.

## 2.1. Git Installation

`Git` is a distributed version control system that tracks changes in the source code and supports collaboration among multiple developers. Git is essential for managing the source code and version control of the Open DID project.

After a successful installation, you can check the version of Git with the following command:
```bash
git --version
```

> **Reference Links**
> - [Git Installation Guide](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository)

<br/>

## 2.2. PostgreSQL Installation
To run the Wallet server, a database installation is required, and Open DID uses PostgreSQL.

> **Reference Links**
- [PostgreSQL Installation Guide](https://www.postgresql.org/download/)
- [8. Installing PostgreSQL with Docker](#8-installing-postgresql-with-docker)

<br/>


# 3. Cloning the Source Code from GitHub

## 3.1. Cloning the Source Code

The `git clone` command allows you to copy the source code from a remote repository hosted on GitHub to your local computer. By using this command, you can work on the entire source code and related files locally. After cloning, you can proceed with the necessary tasks within the repository and push any changes back to the remote repository.

Open the terminal and run the following commands to copy the Wallet server repository to your local computer:
```bash
# Clone the repository from the Git repository
git clone https://github.com/OmniOneID/did-wallet-server.git

# Navigate to the cloned repository
cd did-wallet-server
```

> **Reference Links**
> - [Git Clone Guide](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository)

<br/>

## 3.2. Directory Structure
The main directory structure of the cloned project is as follows:

```
did-wallet-server
├── CHANGELOG.md
├── CLA.md
├── CODE_OF_CONDUCT.md
├── CONTRIBUTING.md
├── dependencies-license.md
├── MAINTAINERS.md
├── README.md
├── RELEASE-PROCESS.md
├── SECURITY.md
├── docs
│   ├── api
│       └── Wallet_API.md
│   ├── errorCode
│       └── Wallet_ErrorCode.md
│   ├── installation
│       └── OpenDID_WalletServer_InstallationAndOperation_Guide.md
│   └── db
│       └── OpenDID_TableDefinition_Wallet.md
└── source
    └── wallet
        ├── gradle
        ├── libs
            └── did-sdk-common-2.0.0.jar
            └── did-blockchain-sdk-server-2.0.0.jar
            └── did-core-sdk-server-2.0.0..jar
            └── did-crypto-sdk-server-2.0.0.jar
            └── did-datamodel-server-2.0.0.jar
            └── did-wallet-sdk-server-2.0.0.jar
            └── did-zkp-sdk-server-2.0.0.jar
        ├── sample
        └── src
        └── build.gradle
        └── README.md
```

| Name                    | Description                                  |
| ----------------------- | -------------------------------------------- |
| CHANGELOG.md            | Changes by version for the project           |
| CODE_OF_CONDUCT.md      | Code of conduct for contributors             |
| CONTRIBUTING.md         | Contribution guidelines and procedures       |
| LICENSE                 | Licens                                       |
| dependencies-license.md | License information for project dependencies |
| MAINTAINERS.md          | Guidelines for project maintainers           |
| RELEASE-PROCESS.md      | Procedure for releasing a new version        |
| SECURITY.md             | Security policy and vulnerability reporting  |
| docs                    | Documentation                                |
| ┖ api                   | API guide documents                          |
| ┖ errorCode             | Error codes and troubleshooting guide        |
| ┖ installation          | Installation and configuration guide         |
| ┖ db                    | Database ERD and table specifications        |
| source                  | Source code                                  |
| ┖ did-wallet-server     | Wallet server source code and build files    |
| ┖ gradle                | Gradle build configuration and scripts       |
| ┖ libs                  | External libraries and dependencies          |
| ┖ sample                | Sample files                                 |
| ┖ src                   | Main source code directory                   |
| ┖ build.gradle          | Gradle build configuration file              |
| ┖ README.md             | Source code overview and instructions        |

<br/>


# 4. How to Run the Server
This chapter explains three methods to run the server.

The project source is located under the `source` directory, and depending on the method, you need to load and configure the source from that directory.

1. **Using an IDE**: You can open the project in an Integrated Development Environment (IDE), set up the run configuration, and directly run the server. This method is useful when quickly testing code changes during development.

2. **Using console commands after building**: After building the project, you can run the server by executing the generated JAR file with a console command (`java -jar`). This method is mainly used when deploying or running the server in a production environment.

3. **Building with Docker**: You can build the server as a Docker image and run it as a Docker container. This method ensures consistency across environments and makes deployment and scaling easier.
   
## 4.1. Running with IntelliJ IDEA (Gradle Support)

IntelliJ IDEA is a widely used Integrated Development Environment (IDE) for Java development. It supports build tools like Gradle, making project setup and dependency management very convenient. Since the Open DID server is built using Gradle, you can easily configure and run the server within IntelliJ IDEA.

### 4.1.1. Installing and Setting Up IntelliJ IDEA
1. Install IntelliJ. (Refer to the link below for installation instructions)

> **Reference Links**
> - [Download IntelliJ IDEA](https://www.jetbrains.com/idea/download/)

### 4.1.2. Opening the Project in IntelliJ
- Launch IntelliJ and select `File -> New -> Project` from Existing Sources. When the file selection window appears, choose the 'source/did-wallet-server' folder from the repository cloned in [3.1. Cloning the Source Code](#31-cloning-the-source-code)
- When the project opens, the `build.gradle` file will be automatically recognized.
- Gradle will automatically download the necessary dependency files. Please wait until this process is completed.

### 4.1.3. Gradle Build
- In the Gradle tab in IntelliJ IDEA, run `Tasks -> build -> build`.
- Once the build is successfully completed, the project will be ready for execution.

### 4.1.4. Running the Server
- In the Gradle tab of IntelliJ IDEA, select `Tasks -> application -> bootRun` and run it.
- Gradle will automatically build and run the server.
- Check the console log for the message "Started [ApplicationName] in [time] seconds" to confirm that the server has started successfully.
- Once the server is running properly, open your browser and navigate to http://localhost:8095/swagger-ui/index.html to verify that the API documentation is displayed correctly through Swagger UI.

> **Note**
> - The Wallet server is initially configured to use the `sample` profile.
> - When the sample profile is set, the server will run without required configurations (e.g., database). For more details, refer to [6. Profile Configuration and Usage](#6-profile-configuration-and-usage)


### 4.1.5. Database Installation
The Wallet server stores the necessary data for operation in a database, so a database must be installed to run the server. The Open DID server uses the PostgreSQL database. There are several ways to install the PostgreSQL server, but using Docker is the simplest and easiest method. For PostgreSQL installation instructions, please refer to [2.2. PostgreSQL Installation](#22-postgresql-installation)

<br/>

### 4.1.6. Server Configuration
- The server settings must be modified according to the deployment environment to ensure stable operation. For example, you need to adjust configuration elements such as database connection information, port numbers, email integration details, etc., based on the specific environment.
- The server configuration files are located at `src/main/resource/config.`
- For detailed configuration instructions, please refer to [5. Configuration Guide.](#5-configuration-guide)

<br/>

## 4.2. Running the Server Using Console Commands

This section provides instructions on how to run the Open DID server using console commands. It explains the process of building the project with Gradle and running the server using the generated JAR file.

### 4.2.1. Gradle Build Command

- Build the source using `gradlew`:
  ```shell
    # Navigate to the source folder of the cloned repository
    cd source/did-wallet-server

    # Grant execute permission to the Gradle Wrapper
    chmod 755 ./gradlew

    # Clean build the project (delete previous build files and rebuild)
    ./gradlew clean build
  ```
  > Note
  > - `gradlew` is short for Gradle Wrapper, a script used to run Gradle in a project. Even if Gradle is not installed locally, it automatically downloads and runs the version of Gradle specified by the project, allowing developers to build the project in a consistent environment, regardless of whether Gradle is installed.

- Navigate to the build folder and confirm that the JAR file has been generated:
    ```shell
      cd build/libs
      ls
    ```
- This command generates the file `did-wallet-server-2.0.0.jar`.

<br/>

### 4.2.2. Running the Server
Run the server using the built JAR file:

```bash
java -jar did-wallet-server-2.0.0.jar
```

> **Note**
> - The Wallet server is initially configured to use the `sample` profile.
> - When the sample profile is set, the server will run without required configurations (e.g., database). For more details, refer to [6. Profile Configuration and Usage](#6-profile-configuration-and-usage)

<br/>

### 4.2.3. Database Installation
The Wallet server stores the necessary data for operation in a database, so a database must be installed to run the server. The Open DID server uses the PostgreSQL database. There are several ways to install the PostgreSQL server, but using Docker is the simplest and easiest method. For PostgreSQL installation instructions, please refer to [2.2. PostgreSQL Installation](#22-postgresql-installation)

<br/>

### 4.2.4. Server Configuration
- The server settings must be modified according to the deployment environment to ensure stable operation. For example, you need to adjust configuration elements such as database connection information, port numbers, email integration details, etc., based on the specific environment.
- The server configuration files are located at `src/main/resource/config.`
- For detailed configuration instructions, please refer to [5. Configuration Guide.](#5-configuration-guide)

<br/>

## 4.3. Running with Docker
- For the process of building, configuring, and running a Docker image, please refer to [7. Running After Building with Docker](#7-running-after-building-with-docker)

<br/>

# 5. Configuration Guide.

This chapter provides guidance on each configuration value included in all of the server's configuration files. Each setting is a crucial element that controls the server's behavior and environment, so proper configuration is necessary for stable server operation. Refer to the descriptions and examples for each item to apply the appropriate settings for your environment.

Please note that settings with the 🔒 icon are fixed by default or generally do not require modification.

## 5.1. application.yml
- Role: The application.yml file defines the basic configuration for the Spring Boot application. Through this file, you can specify various environment variables such as the application's name, database settings, and profile configurations, all of which have a significant impact on the application's behavior.

- Location: `src/main/resources/config`
  
### 5.1.1. Spring Basic Settings
The basic settings in Spring define the application's name and the active profile, playing a critical role in configuring the server's runtime environment.

* `spring.application.name`: 🔒
    - Specifies the name of the application.
    - Purpose: Primarily used for identifying the application in log messages, monitoring tools, or within Spring Cloud services.
    - Example: `wallet`
  
* `spring.profiles.active`:  
  - Defines the active profile.
  - urpose: Selects either the sample or development environment and loads the corresponding settings. For more details on profiles, refer to [6. Profile Configuration and Usage](#6-profile-configuration-and-usage)
  - Supported profiles: sample, dev
  - Example: `sample`, `dev`

* `spring.profiles.group.dev`: 🔒
  - Defines individual profiles included in the `dev` profile group.
  - Purpose: Manages configurations for the development environment.
  - Profile file naming convention: The configuration files corresponding to each profile must use the exact name as defined in the group. For example, the auth profile is written as application-auth.yml, and the databases profile as application-databases.yml. The file names must match the names listed under group.dev.

* `spring.profiles.group.sample`: 🔒
  - Defines individual profiles included in the `sample` profile group.
  - Purpose: Manages configurations for the sample environment.
  - Profile file naming convention: The configuration files corresponding to each profile must use the exact name as defined in the group. For example, the auth profile is written as application-auth.yml, and the databases profile as application-databases.yml. The file names must match the names listed under group.dev.

<br/>

### 5.1.2. Jackson Basic Settings

Jackson is the default JSON serialization/deserialization library used in Spring Boot. By configuring Jackson, you can adjust the serialization format and methods for JSON data, which can improve performance and efficiency during data transmission.

* `spring.jackson.default-property-inclusion`: 🔒 
    - Configures the application to exclude properties from serialization when their value is null.
    - Example: non_null

* `spring.jackson.default-property-inclusion`: 🔒 
    - Prevents errors from being thrown when serializing empty objects.
    - Example: false

<br/>
     
### 5.1.3. Server Settings
The server settings define the port number on which the application will listen for requests.

* `server.port`:  
    - The port number on which the application will run. The default port for the Wallet server is 8095.
    - Example : 8095

<br/>

### 5.1.4. TAS Settings 
The Wallet service communicates with the TAS server. You can configure the address of the TAS server you have set up.
* `tas.url`:  
    - The URL of the TAS (Trust Anchor Service). It can be used for authentication or trust verification.
    - Example: `http://localhost:8090/contextpath/tas`

<br/>

## 5.2. application-logging.yml
- Role: Configures log groups and log levels. This configuration file allows you to define log groups for specific packages or modules and specify log levels individually for each group.

- Location: `src/main/resources/config`
- 
### 5.2.1. Logging Configuration
* `logging.level`: 
    - Specifies the log level.
    - By setting the level to debug, you will be able to see all log messages at the DEBUG level or higher (INFO, WARN, ERROR, FATAL) for the specified packages.

<br/>

## 5.3. database.yml
- Role: Defines how the server manages and operates the database, including connection information, migration settings using Liquibase, and JPA configurations.

- Location: `src/main/resources/config`
  
### 5.3.1. Spring Liquibase Settings
Liquibase is a tool for managing database migrations, helping to track and apply changes to the database schema automatically. This ensures consistency between development and production environments.

* `spring.liquibase.change-log`: 🔒 
    - Specifies the location of the database change log file. Liquibase uses this log file to track and apply database schema changes.
    - Example: `classpath:/db/changelog/master.xml`

* `spring.liquibase.enabled`: 🔒 
    - Controls whether Liquibase is enabled. When set to true, Liquibase runs on application startup and performs database migrations. The sample profile does not require database integration, so this should be set to false.
    - Example: `true` [dev], `false` [sample]

* `spring.liquibase.fall-on-error`: 🔒
    - Controls the behavior when errors occur during database migration by Liquibase. This should only be configured in the `sample` profile.
    - Example: `false` [sample]

<br/>

### 5.3.2. DataSource Settings
The DataSource settings define the basic information required for the application to connect to the database. This includes details such as the database driver, URL, username, and password.

* `spring.datasource.driver-class-name`: 🔒
    - Specifies the database driver class to use. This is the JDBC driver used to connect to the database.
    - Example: `org.postgresql.Driver`

* `spring.datasource.url`:  
    - The database connection URL. This specifies the location and name of the database to which the application will connect.
    - Example: `jdbc:postgresql://localhost:5435/wallet`

* `spring.datasource.username`:  
    - The username for connecting to the database.
    - Example: `wallet`

* `spring.datasource.password`:  
    - The password for connecting to the database.
    - Example: `walletpassword`

<br/>

### 5.3.3. JPA Settings
JPA settings control how the application interacts with the database, significantly impacting performance and readability.

* `spring.jpa.open-in-view`: 🔒 
    - Configures whether to use the OSIV (Open Session In View) pattern. When set to true, the database connection is kept open for the entire HTTP request.
    - Example: `true`

* `spring.jpa.show-sql`: 🔒 
    - SConfigures whether to log SQL queries. When set to true, the executed SQL queries are output to the log, which is useful for debugging during development.
    - Example: `true`

* `spring.jpa.hibernate.ddl-auto`: 🔒 
    - Configures Hibernate's DDL auto-generation mode. This specifies the strategy for automatically creating the database schema. Setting it to none disables auto-generation.
    - Example: `none`

* `spring.jpa.hibernate.naming.physical-strategy`: 🔒 
    - Configures the naming strategy for database objects. This defines how the entity class names are converted into database table names.
    - Example: `org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy`

* `spring.jpa.properties.hibernate.format_sql`: 🔒 
    - Configures whether to format SQL. When set to false, SQL queries printed in the log are not formatted.
    - Example: `false`

<br/>

## 5.4. wallet.yml
- Role: Configures the wallet file information used by the server.

- Location: `src/main/resources/config`

### 5.4.1. Wallet Access Information Configuration

* `wallet.file-path`:  
    - Specifies the path to the wallet file. This defines the location where the file wallet is stored. The file may contain sensitive information, such as private keys. **It must be specified as an absolute path.**
    - Example: `/path/to/your/wallet.wallet`

* `wallet.password`:  
    - The password used to access the wallet. This password is required to access the wallet file and should be highly secure.
    - Example: `your_secure_wallet_password`

<br/>

## 5.5. blockchain.properties
- Role: Configures blockchain server information to be integrated with the CA server. When you install the Hyperledger Besu network according to '5.3. Step 3: Blockchain Installation' in the [Open DID Installation Guide], private keys, certificates, and server connection information configuration files are automatically generated. In blockchain.properties, you configure the paths where these files are located and the network name entered during Hyperledger Besu installation.

- Location: `src/main/resources/properties`

### 5.5.1. Blockchain Integration Configuration 

#### EVM Network Configuration

- `evm.network.url:`:
  - EVM Network address. When running Besu locally on the same machine as the client, use this fixed value. (Default Port: 8545)
  - Example: http://localhost:8545

- `evm.chainId:`:
  - Chain ID identifier. Currently using a fixed value of 1337. (Default Value: 1337)
  - Example: 1337

- `evm.gas.limit:`:
  - Maximum gas limit allowed for Hyperledger Besu EVM transactions. Currently used as fixed value for Free Gas. (Default Value: 100000000)
  - Example: 100000000

- `evm.gas.price :`:
  - Gas price per unit. Currently used as fixed value 0 for Free Gas. (Default Value: 0)
  - Example: 0

- `evm.connection.timeout:`: 
  - Network connection timeout value (milliseconds). Currently using the recommended fixed value of 10000. (Default Value: 10000)
  - Example: 10000

#### EVM Contract Configuration

- `evm.connection.address:`: 
  - Address value of the OpenDID Contract returned when deploying Smart Contract with Hardhat. For detailed guide, refer to [DID Besu Contract].
  - Example: 0xa0E49611FB410c00f425E83A4240e1681c51DDf4

- `evm.connection.privateKey:`: 
  - k1 key used for API access control. Enter the key string defined in accounts inside hardhat.config.js (remove the 0x prefix) to enable API calls with Owner permissions (Default setting). For detailed guide, refer to [DID Besu Contract].
  - Example: 0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63

<br/>

# 6. Profile Configuration and Usage

## 6.1. Profile Overview (`sample`, `dev`)
The Wallet server supports two profiles, `dev` and `sample`, to allow operation in various environments.

Each profile is designed to apply the appropriate settings for its environment. By default, the Wallet server is configured to use the `sample` profile, which is designed to run the server independently without connecting to external services such as a database or blockchain. The `sample` profile is ideal for testing API calls, allowing developers to quickly verify the basic functionality of the application. This profile returns fixed response data for all API calls, making it useful in early development environments.

Sample API calls are written as JUnit tests, which can be referenced when writing your own tests.

On the other hand, the `dev` profile is designed to perform actual operations. When using this profile, testing and validation with real data are possible. Activating the dev profile enables integration with external services such as databases and blockchain, allowing you to test the application's behavior in a real-world environment.

### 6.1.1. `sample` Profile
The `sample` profile is designed to run the server independently without connecting to external services (e.g., databases, blockchain). This profile is suitable for testing API calls, allowing developers to quickly verify the basic functionality of the application. It returns fixed response data for all API calls, making it useful in the early development stage or for functional testing. Since no integration with external systems is required, it provides an environment where the server can be run and tested standalone.

### 6.1.2. `dev` Profile
The `dev` profile includes settings suited for the development environment and is used on development servers. To use this profile, you will need configuration for the development environment's database and blockchain node.

<br/>

## 6.2. How to Configure Profiles
This section explains how to switch profiles for each server operation method.

### 6.2.1. Running the Server Using an IDE
- **Select Configuration File:**  In the `src/main/resources path`, select the `application.yml` file.
- **Specify Profile:** In the IDE's Run/Debug Configurations, add the option `--spring.profiles.active={profile}` to activate the desired profile.
- **Apply Configuration:** The configuration file corresponding to the activated profile will be applied.

### 6.2.2. Running the Server Using Console Commands
- **Select Configuration File:** Prepare the profile-specific configuration files in the same directory as the built JAR file or in the directory where the configuration files are located.
- **Specify Profile:** Add the option `--spring.profiles.active={profile}` to the server startup command to activate the desired profile.
  
  ```bash
  java -jar build/libs/did-wallet-server-2.0.0.jar --spring.profiles.active={profile}
  ```

- **Apply Configuration:** The configuration file corresponding to the activated profile will be applied.

### 6.2.3. Running the Server Using Docker
- **Select Configuration File:** When creating a Docker image, specify the configuration file path in the Dockerfile, or mount external configuration files to the Docker container.
- **Specify Profile:** Set the `SPRING_PROFILES_ACTIVE` environment variable in the Docker Compose file or the Docker run command to specify the profile.
  
  ```yaml
  environment:
    - SPRING_PROFILES_ACTIVE={profile}
  ```

- **Apply Configuration:** The specified profile's configuration will be applied when the Docker container is executed.

By following these methods, you can flexibly switch profile-specific settings and easily apply the appropriate configuration for your project environment.


# 7. Running After Building with Docker

## 7.1. Docker Image Build Method (Based on `Dockerfile`)

### 7.1.1. Build Docker image
Build the Docker image with the following command:

```bash
cd {source_directory}
docker build -t did-wallet-server -f did-wallet-server/Dockerfile .
```

<br/>

## 7.2. Running with Docker Compose

### 7.2.1. Preparing Directories and Configuration Files

#### 1. Create docker-compose directory and config directory
```bash
mkdir -p {docker_compose_directory}/config
```

#### 2. Copy configuration files (yml) to config directory
```bash
cp {application_yml_directory}/* {docker_compose_directory}/config/
cp {blockchain_properties_path} {docker_compose_directory}/config/
```

#### 3. Modify blockchain.properties file
```yml
evm.network.url=http://host.docker.internal:8545
... (omitted)
```

> **host.docker.internal** is a special address that points to the host machine from within a Docker container.  
> Since localhost inside a container refers to the container itself, you must use host.docker.internal to access services (PostgreSQL, blockchain) running on the host.

#### 4. Modify application-database.yml file
```yml
spring:
 ... (omitted)
 datasource:
   driver-class-name: org.postgresql.Driver
   url: jdbc:postgresql://host.docker.internal:5430/wallet
   username: omn
   password: omn
 ... (omitted)
```

### 7.2.2. Create `docker-compose.yml` file
You can easily manage multiple containers using the `docker-compose.yml` file.

```yml
version: '3'
services:
 app:
   image: did-wallet-server
   ports:
     - "8095:8095"
   volumes:
     - {config_directory}:/app/config
   environment:
     - SPRING_PROFILES_ACTIVE=dev
   extra_hosts:
     - "host.docker.internal:host-gateway"
```

> - In the example above, the `config_directory` is mounted to `/app/config` inside the container to share configuration files.
>   - Configuration files located in `config_directory` take priority over default configuration files.
>   - For detailed configuration instructions, please refer to [5. Configuration Guide](#5-configuration-guide).


### 7.2.3. Run Container
```bash
cd {docker_compose_directory}
docker-compose up -d
```

<br/>

# 8. Installing PostgreSQL with Docker

This section explains how to install PostgreSQL using Docker. By following this method, you can easily install PostgreSQL and integrate it with your server for use.

## 8.1. Installing PostgreSQL Using Docker Compose

The following is a method to install PostgreSQL using Docker Compose:

```yml
services:
  postgres:
    container_name: postgre-wallet
    image: postgres:16.4
    restart: always
    volumes:
      - postgres_data_wallet:/var/lib/postgresql/data
    ports:
      - 5435:5432
    environment:
      POSTGRES_USER: ${USER}
      POSTGRES_PASSWORD: ${PW}
      POSTGRES_DB: wallet
  
volumes:
  postgres_data:
```

This Docker Compose file installs PostgreSQL version 16.4 and applies the following settings:

- **container_name**: Sets the container name to `postgre-wallet`.
- **volumes**: Mounts the `postgres_data_wallet` volume to PostgreSQL's data directory (`/var/lib/postgresql/data`), ensuring data persistence.
- **ports**: Maps port 5435 on the host to port 5432 on the container.
- **environment**: Configures the PostgreSQL username, password, and database name. `${USER}` and `${PW}` can be set as environment variables.

## 8.2. Running the PostgreSQL Container

To run the PostgreSQL container using the Docker Compose file above, execute the following command in your terminal:

```bash
docker-compose up -d
```

This command runs the PostgreSQL container in the background. The PostgreSQL server will start based on the configured environment variables, and the database will be prepared. You can proceed with the integration settings to allow your application to use this database.

<!-- References -->
[Open DID Installation Guide]: https://github.com/OmniOneID/did-release/blob/main/release-V2.0.0.0/OpenDID_Installation_Guide-V2.0.0.0.md
[Open DID Admin Console Guide]: ../admin/OpenDID_WalletAdmin_Operation_Guide_ko.md
[DID Besu Contract]: https://github.com/OmniOneID/did-besu-contract
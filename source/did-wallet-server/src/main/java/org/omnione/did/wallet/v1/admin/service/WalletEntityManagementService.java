/*
 * Copyright 2025 OmniOne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omnione.did.wallet.v1.admin.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.base.db.constant.WalletServiceStatus;
import org.omnione.did.base.db.domain.CertificateVc;
import org.omnione.did.base.db.domain.EntityDidDocument;
import org.omnione.did.base.db.domain.WalletServiceInfo;
import org.omnione.did.base.db.repository.WalletServiceRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.response.ErrorResponse;
import org.omnione.did.base.utils.BaseCoreDidUtil;
import org.omnione.did.base.utils.BaseMultibaseUtil;
import org.omnione.did.common.exception.HttpClientException;
import org.omnione.did.common.util.HttpClientUtil;
import org.omnione.did.common.util.JsonUtil;
import org.omnione.did.core.data.rest.DidKeyInfo;
import org.omnione.did.core.manager.DidManager;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.enums.did.ProofPurpose;
import org.omnione.did.data.model.enums.vc.RoleType;
import org.omnione.did.data.model.vc.VerifiableCredential;
import org.omnione.did.wallet.key.WalletManagerInterface;
import org.omnione.did.wallet.v1.admin.constant.EntityStatus;
import org.omnione.did.wallet.v1.admin.dto.walletservice.*;
import org.omnione.did.wallet.v1.admin.dto.admin.GetWalletEntityInfoReqDto;
import org.omnione.did.wallet.v1.admin.service.query.DidDocumentQueryService;
import org.omnione.did.wallet.v1.admin.service.query.WalletServiceQueryService;
import org.omnione.did.wallet.v1.agent.service.EnrollEntityService;
import org.omnione.did.wallet.v1.agent.service.FileWalletService;
import org.omnione.did.wallet.v1.admin.service.JsonParseService;
import org.omnione.did.wallet.v1.agent.service.StorageService;
import org.omnione.did.wallet.v1.agent.service.query.CertificateVcQueryService;
import org.omnione.did.wallet.v1.common.dto.EmptyResDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service responsible for managing wallet entity lifecycle operations in the Admin Console.
 * <p>
 * Handles tasks such as wallet service registration, DID generation, entity enrollment, and status verification
 * by communicating with the TAS (Trusted Authority Service).
 */
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class WalletEntityManagementService {
    private final WalletServiceQueryService walletServiceQueryService;
    private final StorageService storageService;
    private final WalletServiceRepository walletServiceRepository;
    private final CertificateVcQueryService certificateVcQueryService;
    private final JsonParseService jsonParseService;
    private final FileWalletService fileWalletService;
    private final DidDocumentQueryService didDocumentQueryService;
    private final EnrollEntityService enrollEntityService;

    @Value(value = "${tas.url}")
    private String TAS_URL;

    /**
     * Retrieves wallet service information for the Admin Console.
     * <p>
     * If the DID Document is required or not registered, only basic wallet info is returned.
     * Otherwise, the full wallet service info along with the DID Document is returned.
     *
     * @return Wallet service information DTO, optionally including the DID Document
     */
    public GetWalletEntityInfoReqDto getWalletEntityInfo() {
        WalletServiceInfo existingWalletService = walletServiceQueryService.findWalletServiceOrNull();

        if (existingWalletService == null || existingWalletService.getStatus() == WalletServiceStatus.DID_DOCUMENT_REQUIRED) {
            return GetWalletEntityInfoReqDto.fromEntity(existingWalletService);
        }

        DidDocument didDocument = storageService.findDidDoc(existingWalletService.getDid());
        return GetWalletEntityInfoReqDto.fromEntity(existingWalletService, didDocument);
    }

    /**
     * Stores a decoded Certificate VC in the system.
     * <p>
     * Decodes the base-multibase VC string and saves it to the certificate VC repository.
     *
     * @param sendCertificateVcReqDto DTO containing the encoded Certificate VC
     * @return an empty response DTO upon success
     */
    public EmptyResDto createCertificateVc( SendCertificateVcReqDto sendCertificateVcReqDto) {
        byte[] decodedVc = BaseMultibaseUtil.decode(sendCertificateVcReqDto.getCertificateVc());
        log.debug("Decoded VC: {}", new String(decodedVc));

        certificateVcQueryService.save(CertificateVc.builder()
                .vc(new String(decodedVc))
                .build());

        return new EmptyResDto();
    }

    /**
     * Updates or registers the wallet service entity's basic information.
     * <p>
     * If the wallet service does not exist, it creates a new one. Otherwise, it updates the existing entry.
     * This includes updating DID, server URL, certificate URL, and status.
     *
     * @param sendEntityInfoReqDto DTO containing the wallet service info to be updated
     * @return an empty response DTO upon success
     */
    public EmptyResDto updateEntityInfo(SendEntityInfoReqDto sendEntityInfoReqDto) {
        WalletServiceInfo existingWalletService = walletServiceQueryService.findWalletService();

        if (existingWalletService == null) {
            walletServiceRepository.save(WalletServiceInfo.builder()
                    .name(sendEntityInfoReqDto.getName())
                    .did(sendEntityInfoReqDto.getDid())
                    .status(WalletServiceStatus.ACTIVATE)
                    .serverUrl(sendEntityInfoReqDto.getServerUrl())
                    .certificateUrl(sendEntityInfoReqDto.getCertificateUrl())
                    .build());
        } else {
            existingWalletService.setName(sendEntityInfoReqDto.getName());
            existingWalletService.setDid(sendEntityInfoReqDto.getDid());
            existingWalletService.setServerUrl(sendEntityInfoReqDto.getServerUrl());
            existingWalletService.setCertificateUrl(sendEntityInfoReqDto.getCertificateUrl());
            existingWalletService.setStatus(WalletServiceStatus.ACTIVATE);
            walletServiceRepository.save(existingWalletService);
        }

        return new EmptyResDto();
    }

    /**
     * Register Wallet Service Info information.
     *
     * @param registerWalletInfoReqDto Request data transfer object
     * @return Issuer information
     */
    public WalletServiceInfoResDto registerWalletInfo(RegisterWallerServiceInfoReqDto registerWalletInfoReqDto) {
        log.debug("=== Starting registerIssuerInfo ===");

        WalletServiceInfo walletService = walletServiceQueryService.findWalletServiceOrNull();
        log.debug("\t--> Found Issuer: {}", walletService);

        if (walletService == null) {
            log.debug("\t--> Issuer is not registered yet. Proceeding with new registration.");
            walletService = WalletServiceInfo.builder()
                    .name(registerWalletInfoReqDto.getName())
                    .serverUrl(registerWalletInfoReqDto.getServerUrl())
                    .certificateUrl(registerWalletInfoReqDto.getServerUrl() + "/api/v1/certificate-vc")
                    .status(WalletServiceStatus.DID_DOCUMENT_REQUIRED)
                    .build();
            walletServiceRepository.save(walletService);

            log.debug("=== Finished registerIssuerInfo ===");
            return buildIssuerInfoResponse(walletService);
        }

        if (walletService.getStatus() == WalletServiceStatus.ACTIVATE) {
            log.error("Issuer is already registered");
            throw new OpenDidException(ErrorCode.WALLET_SERVICE_ALREADY_REGISTERED);
        }

        log.debug("\t--> Updating Issuer information");
        walletService.setName(registerWalletInfoReqDto.getName());
        walletService.setServerUrl(registerWalletInfoReqDto.getServerUrl());
        walletService.setCertificateUrl(registerWalletInfoReqDto.getServerUrl() + "/api/v1/certificate-vc");
        walletServiceRepository.save(walletService);

        log.debug("=== Finished registerIssuerInfo ===");

        return buildIssuerInfoResponse(walletService);
    }

    private WalletServiceInfoResDto buildIssuerInfoResponse(WalletServiceInfo walletService) {
        if (walletService.getStatus() == WalletServiceStatus.DID_DOCUMENT_REQUIRED
                || walletService.getStatus() == WalletServiceStatus.DID_DOCUMENT_REQUESTED) {
            return WalletServiceInfoResDto.fromEntity(walletService);
        }

        log.debug("\t--> Finding TAS DID Document");
        DidDocument didDocument = storageService.findDidDoc(walletService.getDid());
        return WalletServiceInfoResDto.fromEntity(walletService, didDocument);
    }


    /**
     * Register Issuer DID Document automatically.
     * <p>
     * This method creates a wallet, generates keys, and creates a DID Document.
     *
     * @return Map containing the generated DID Document
     */
    public Map<String, Object> registerWalletDidDocumentAuto() {
        log.debug("=== Starting registerIssuerDidDocumentAuto ===");

        // Finding Issuer
        log.debug("\t--> Finding Wallet Service");
        WalletServiceInfo existedWalletService = walletServiceQueryService.findWalletService();
        log.debug("\t--> Found Wallet Service: {}", existedWalletService);

        // Check Wallet Service status
        if (existedWalletService.getStatus() != WalletServiceStatus.DID_DOCUMENT_REQUIRED) {
            if (existedWalletService.getStatus() == WalletServiceStatus.DID_DOCUMENT_REQUESTED) {
                log.error("Wallet Service DID Document is already requested");
                throw new OpenDidException(ErrorCode.WALLET_SERVICE_DID_DOCUMENT_ALREADY_REQUESTED);
            }
            log.error("Wallet Service DID Document is already registered");
            throw new OpenDidException(ErrorCode.WALLET_SERVICE_DID_DOCUMENT_ALREADY_REGISTERED);
        }

        // Step1: Create Wallet and keys
        WalletManagerInterface walletManager = initializeWalletWithKeys();

        // Step2: Create DID Document
        DidDocument didDocument = createDidDocumentAuto(walletManager);

        log.debug("=== Finished registerIssuerDidDocumentAuto ===");

        return jsonParseService.parseDidDocToMap(didDocument.toJson());
    }
    /*
     * Generate Issuer wallet and keys.
     */
    public WalletManagerInterface initializeWalletWithKeys() {
        return fileWalletService.initializeWalletWithKeys();
    }

    /**
     * Create DID Document automatically.
     * <p>
     * This method creates a DID Document using the provided wallet manager.
     *
     * @param walletManager Wallet manager
     * @return Created DID Document
     */
    public DidDocument createDidDocumentAuto(WalletManagerInterface walletManager) {
        String did = "did:omn:wallet";

        Map<String, List<ProofPurpose>> purposes = BaseCoreDidUtil.createDefaultProofPurposes();
        List<DidKeyInfo> keyInfos = BaseCoreDidUtil.getDidKeyInfosFromWallet(walletManager, did, purposes);

        DidManager didManager = new DidManager();
        DidDocument unsignedDoc = BaseCoreDidUtil.createDidDocument(didManager, did, did, keyInfos);

        List<String> signingKeys = BaseCoreDidUtil.getSigningKeyIds(purposes);
        DidDocument signedDoc = BaseCoreDidUtil.signAndAddProof(didManager, walletManager, signingKeys);

        return signedDoc;
    }

    public EmptyResDto requestRegisterDid(RequestRegisterDidReqDto request) {
        try {
            log.debug("=== Starting requestRegisterDid ===");

            WalletServiceInfo walletServiceInfo = walletServiceQueryService.findWalletService();
            log.debug("\t--> Found Issuer: {}", walletServiceInfo);

            if (walletServiceInfo.getStatus() != WalletServiceStatus.DID_DOCUMENT_REQUIRED) {
                if (walletServiceInfo.getStatus() == WalletServiceStatus.DID_DOCUMENT_REQUESTED) {
                    log.error("Issuer DID Document is already requested");
                    throw new OpenDidException(ErrorCode.WALLET_SERVICE_DID_DOCUMENT_ALREADY_REQUESTED);
                }
                log.error("Issuer DID Document is already registered");
                throw new OpenDidException(ErrorCode.WALLET_SERVICE_DID_DOCUMENT_ALREADY_REGISTERED);
            }

            // Send the register DID request to TAS
            log.debug("\t--> Sending register DID request to TAS");
            EmptyResDto resDto = sendRegisterDid(walletServiceInfo, request);

            // Update didDocument in the database
            log.debug("\t--> Updating DID Document in the database");
            EntityDidDocument entityDidDoc = didDocumentQueryService.findDidDocumentOrNull();
            if (entityDidDoc == null) {
                entityDidDoc = EntityDidDocument.builder()
                        .didDocument(request.getDidDocument())
                        .build();
            } else {
                entityDidDoc.setDidDocument(request.getDidDocument());
            }
            didDocumentQueryService.save(entityDidDoc);

            // Update Issuer status
            log.debug("\t--> Updating Issuer did and status");
            walletServiceInfo.setStatus(WalletServiceStatus.DID_DOCUMENT_REQUESTED);
            walletServiceInfo.setDid(BaseCoreDidUtil.parseDid(request.getDidDocument()));
            walletServiceRepository.save(walletServiceInfo);

            log.debug("=== Finished requestRegisterDid ===");

            return resDto;
        } catch (OpenDidException e) {
            log.error("Failed to register Issuer DID Document: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to register Issuer DID Document", e);
            throw new OpenDidException(ErrorCode.FAILED_TO_REGISTER_WALLET_SERVICE_DID_DOCUMENT);
        }
    }
    private EmptyResDto sendRegisterDid(WalletServiceInfo walletServiceInfo, RequestRegisterDidReqDto requestRegisterDidReqDto) {
        String url = TAS_URL + UrlConstant.Tas.ADMIN_V1 + UrlConstant.Tas.REGISTER_DID_PUBLIC;

        String encodedDidDocument = BaseMultibaseUtil.encode(requestRegisterDidReqDto.getDidDocument().getBytes());
        RegisterDidToTaReqDto registerDidToTaReqDto = RegisterDidToTaReqDto.builder()
                .didDoc(encodedDidDocument)
                .name(walletServiceInfo.getName())
                .serverUrl(walletServiceInfo.getServerUrl())
                .certificateUrl(walletServiceInfo.getCertificateUrl())
                .role(RoleType.APP_PROVIDER)
                .build();
        try {
            String request = JsonUtil.serializeToJson(registerDidToTaReqDto);
            return HttpClientUtil.postData(url, request, EmptyResDto.class);
        } catch (HttpClientException e) {
            log.error("HttpClientException occurred while sending register-did-public request: {}", e.getResponseBody(), e);
            ErrorResponse errorResponse = convertExternalErrorResponse(e.getResponseBody());
            throw new OpenDidException(errorResponse);
        } catch (Exception e) {
            log.error("An unknown error occurred while sending register-did-public request", e);
            throw new OpenDidException(ErrorCode.TAS_COMMUNICATION_ERROR);
        }
    }
    /**
     * Converts an external error response string to an ErrorResponse object.
     * This method attempts to parse the given JSON string into an ErrorResponse instance.
     *
     * @param resBody The JSON string representing the external error response
     * @return An ErrorResponse object parsed from the input string
     * @throws OpenDidException with ErrorCode.ISSUER_UNKNOWN_RESPONSE if parsing fails
     */
    private ErrorResponse convertExternalErrorResponse(String resBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(resBody, ErrorResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse external error response: {}", resBody, e);
            throw new OpenDidException(ErrorCode.TAS_COMMUNICATION_ERROR);
        }
    }

    /**
     * Request the status of the entity.
     *
     * @return RequestEntityStatusResDto containing the status information
     */
    public RequestEntityStatusResDto requestEntityStatus() {
        log.debug("=== Starting requestEntityStatus ===");

        // Finding Issuer
        log.debug("\t--> Finding Issuer");
        WalletServiceInfo existedWalletService = walletServiceQueryService.findWalletService();

        String did = existedWalletService.getDid();
        if (did == null) {
            EntityDidDocument entityDidDocument = didDocumentQueryService.findDidDocument();
            DidDocument didDocument = new DidDocument();
            didDocument.fromJson(entityDidDocument.getDidDocument());
            did = didDocument.getId();
        }

        // Sending request-status request to TAS
        log.debug("\t--> Sending request-status request to TAS");
        RequestEntityStatusResDto requestEntityStatusResDto = sendRequestEntityStatus(did);

        // Update Issuer status based on the response
        if (requestEntityStatusResDto.getStatus() == EntityStatus.NOT_REGISTERED) {
            log.debug("\t--> TA has deleted the entity's registration request. Updating Issuer status accordingly");
            existedWalletService.setStatus(WalletServiceStatus.DID_DOCUMENT_REQUIRED);
            walletServiceRepository.save(existedWalletService);
        } else if (requestEntityStatusResDto.getStatus() == EntityStatus.CERTIFICATE_VC_REQUIRED) {
            log.debug("\t--> TA has requested a certificate VC. Updating Issuer status accordingly");
            existedWalletService.setStatus(WalletServiceStatus.CERTIFICATE_VC_REQUIRED);
            walletServiceRepository.save(existedWalletService);
        }

        log.debug("=== Finished requestEntityStatus ===");

        return requestEntityStatusResDto;
    }

    private RequestEntityStatusResDto sendRequestEntityStatus(String did) {
        String url = TAS_URL + UrlConstant.Tas.ADMIN_V1 + UrlConstant.Tas.REQUEST_ENTITY_STATUS + "?did=" + did;

        try {
            return HttpClientUtil.getData(url, RequestEntityStatusResDto.class);
        } catch (HttpClientException e) {
            log.error("HttpClientException occurred while sending request-status request: {}", e.getResponseBody(), e);
            ErrorResponse errorResponse = convertExternalErrorResponse(e.getResponseBody());
            throw new OpenDidException(errorResponse);
        } catch (Exception e) {
            log.error("An unknown error occurred while sending request-status request", e);
            throw new OpenDidException(ErrorCode.TAS_COMMUNICATION_ERROR);
        }
    }

    public Map<String, Object> enrollEntity() {
        try {
            log.debug("=== Starting enrollEntity ===");
            // Register the entity
            log.debug("\t--> Registering the entity");
            enrollEntityService.enrollEntity();

            // Finding Certificate VC
            log.debug("\t--> Finding Certificate VC");
            CertificateVc certificateVc = certificateVcQueryService.findCertificateVc();
            VerifiableCredential verifiableCredential = new VerifiableCredential();
            verifiableCredential.fromJson(certificateVc.getVc());

            log.debug("=== Finished enrollEntity ===");
            return jsonParseService.parseCertificateVcToMap(verifiableCredential.toJson());
        } catch(OpenDidException e) {
            log.error("An OpenDidException occurred while sending requestCertificateVc request", e);
            throw e;
        } catch (Exception e) {
            log.error("An unknown error occurred while sending requestCertificateVc request", e);
            throw new OpenDidException(ErrorCode.FAILED_TO_REQUEST_CERTIFICATE_VC);
        }
    }
}

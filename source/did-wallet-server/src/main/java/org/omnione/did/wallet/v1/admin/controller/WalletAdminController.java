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
package org.omnione.did.wallet.v1.admin.controller;

import lombok.RequiredArgsConstructor;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.wallet.v1.admin.dto.admin.GetWalletEntityInfoReqDto;
import org.omnione.did.wallet.v1.admin.dto.walletservice.*;
import org.omnione.did.wallet.v1.admin.service.WalletEntityManagementService;
import org.omnione.did.wallet.v1.common.dto.EmptyResDto;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for managing Wallet Service entity registration and certificate issuance in the Admin Console.
 * <p>
 * Provides endpoints for retrieving wallet information, issuing certificate VCs, registering entity info,
 * generating and registering DID documents, and checking entity status with TAS.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Admin.V1 + UrlConstant.Admin.WALLET_SERVICE)
public class WalletAdminController {

    private final WalletEntityManagementService walletEntityManagementService;

    /**
     * Retrieves current wallet service entity information.
     *
     * @return wallet entity information DTO
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public GetWalletEntityInfoReqDto getWalletInfo() {
        return walletEntityManagementService.getWalletEntityInfo();
    }

    /**
     * Registers wallet service entity information.
     *
     * @param registerCaInfoReqDto request data to register wallet info
     * @return wallet service registration result
     */
    @RequestMapping(value = "/register-wallet-service-info", method = RequestMethod.POST)
    public WalletServiceInfoResDto registerWalletInfo(@RequestBody RegisterWallerServiceInfoReqDto registerCaInfoReqDto) {
        return walletEntityManagementService.registerWalletInfo(registerCaInfoReqDto);
    }

    /**
     * Automatically generates and stores a DID document for the wallet service.
     *
     * @return map containing generated DID Document
     */
    @RequestMapping(value = "/generate-did-auto", method = RequestMethod.POST)
    public Map<String, Object> generateWalletDidDocumentAuto() {
        return walletEntityManagementService.registerWalletDidDocumentAuto();
    }

    /**
     * Sends a request to register the generated DID with the Trusted Authority.
     *
     * @param requestRegisterDidReqDto request containing DID Document
     * @return empty response DTO
     */
    @RequestMapping(value = "/register-did", method = RequestMethod.POST)
    public EmptyResDto requestRegisterDid(@RequestBody RequestRegisterDidReqDto requestRegisterDidReqDto) {
        return walletEntityManagementService.requestRegisterDid(requestRegisterDidReqDto);
    }

    /**
     * Checks the current registration status of the wallet service entity.
     *
     * @return registration status response DTO
     */
    @GetMapping(value = "/request-status")
    public RequestEntityStatusResDto requestEntityStatus() {
        return walletEntityManagementService.requestEntityStatus();
    }

    /**
     * Sends a request to enroll the wallet service entity with the Trusted Authority.
     *
     * @return map containing enrollment result
     */
    @PostMapping(value = "/request-enroll-entity")
    public Map<String, Object> requestEnrollEntity() {
        return walletEntityManagementService.enrollEntity();
    }
}

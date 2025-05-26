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
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.wallet.v1.admin.dto.wallet.WalletDto;
import org.omnione.did.wallet.v1.admin.dto.walletservice.SendCertificateVcReqDto;
import org.omnione.did.wallet.v1.admin.dto.walletservice.SendEntityInfoReqDto;
import org.omnione.did.wallet.v1.admin.service.WalletEntityManagementService;
import org.omnione.did.wallet.v1.admin.service.WalletManagementService;
import org.omnione.did.wallet.v1.common.dto.EmptyResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing wallet information in the Admin Console.
 * <p>
 * Provides endpoints to search for wallets and retrieve wallet details by ID.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Admin.V1)
public class WalletManagementController {

    private final WalletManagementService walletManagementService;
    private final WalletEntityManagementService walletEntityManagementService;

    /**
     * Searches for wallet records using search criteria with pagination.
     *
     * @param searchKey the field to search by (e.g., wallet name, DID)
     * @param searchValue the value to match
     * @param pageable pagination information
     * @return a page of matching WalletDto objects
     */
    @GetMapping(value = "/wallets/list")
    @ResponseBody
    public Page<WalletDto> searchWallets(String searchKey, String searchValue, Pageable pageable) {
        return walletManagementService.searchWallets(searchKey, searchValue, pageable);
    }

    /**
     * Retrieves detailed information about a specific wallet by ID.
     *
     * @param id the wallet ID
     * @return the WalletDto containing wallet details
     */
    @GetMapping(value = "/wallets")
    public WalletDto getWallet(@RequestParam Long id) {
        return walletManagementService.findById(id);
    }

    /**
     * Issues a certificate VC by decoding and storing it.
     *
     * @param sendCertificateVcReqDto request containing encoded certificate VC
     * @return empty response DTO
     */
    @RequestMapping(value = "/certificate-vc", method = RequestMethod.POST)
    public EmptyResDto createCertificateVc(@RequestBody SendCertificateVcReqDto sendCertificateVcReqDto) {
        return walletEntityManagementService.createCertificateVc(sendCertificateVcReqDto);
    }

    /**
     * Updates the wallet entity information.
     *
     * @param sendEntityInfoReqDto request containing DID, name, and endpoint URLs
     * @return empty response DTO
     */
    @RequestMapping(value = "/entity-info", method = RequestMethod.POST)
    public EmptyResDto updateEntityInfo(@RequestBody SendEntityInfoReqDto sendEntityInfoReqDto) {
        return walletEntityManagementService.updateEntityInfo(sendEntityInfoReqDto);
    }
}

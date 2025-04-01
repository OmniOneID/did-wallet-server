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

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.wallet.v1.admin.dto.walletservice.SendCertificateVcReqDto;
import org.omnione.did.wallet.v1.admin.dto.admin.GetWalletServiceInfoReqDto;
import org.omnione.did.wallet.v1.admin.dto.walletservice.SendEntityInfoReqDto;
import org.omnione.did.wallet.v1.admin.service.WalletServiceManagementService;
import org.omnione.did.wallet.v1.common.dto.EmptyResDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Wallet.ADMIN_V1)
public class WalletServiceManagementController {
    private final WalletServiceManagementService walletServiceManagementService;

    @Operation(summary = "Get Wallet Service Info", description = "get Wallet Service Info")
    @GetMapping("/wallet-service/info")
    public GetWalletServiceInfoReqDto getCasInfo() {
        return walletServiceManagementService.getCasInfo();
    }

    @RequestMapping(value = "/certificate-vc", method = RequestMethod.POST)
    public EmptyResDto createCertificateVc(@RequestBody SendCertificateVcReqDto sendCertificateVcReqDto) {
        return walletServiceManagementService.createCertificateVc(sendCertificateVcReqDto);
    }

    @RequestMapping(value = "/entity-info", method = RequestMethod.POST)
    public EmptyResDto updateEntityInfo(@RequestBody SendEntityInfoReqDto sendEntityInfoReqDto) {
        return walletServiceManagementService.updateEntityInfo(sendEntityInfoReqDto);
    }
}

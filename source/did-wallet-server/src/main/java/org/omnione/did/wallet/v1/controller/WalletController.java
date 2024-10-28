/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.wallet.v1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.base.datamodel.data.AttestedDidDoc;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.wallet.v1.service.WalletService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The WalletController class is a controller that handles the wallet request.
 * It provides endpoints for signing a wallet.
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = UrlConstant.Wallet.V1)
@RestController
public class WalletController {
    private final WalletService walletService;
    /**
     * Sign the given wallet.
     *
     * @param request the wallet to sign
     * @return the signed wallet
     */
    @PostMapping(value = UrlConstant.Wallet.REQUEST_SIGN_WALLET)
    public AttestedDidDoc signWallet(@RequestBody DidDocument request) {
        return walletService.signWallet(request);
    }
}

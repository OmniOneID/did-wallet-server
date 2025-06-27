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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.wallet.v1.admin.dto.wallet.WalletDto;
import org.omnione.did.wallet.v1.agent.service.query.WalletQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service for managing wallet entities in the Admin Console.
 * <p>
 * Provides functionality to search and retrieve wallet records.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WalletManagementService {

    private final WalletQueryService walletQueryService;

    /**
     * Searches wallet records with optional filtering by search key and value.
     *
     * @param searchKey   the field name to search by (e.g. "walletId")
     * @param searchValue the value to match against the specified key
     * @param pageable    pagination configuration
     * @return a page of wallet DTOs that match the search criteria
     */
    public Page<WalletDto> searchWallets(String searchKey, String searchValue, Pageable pageable) {
        return walletQueryService.searchWalletList(searchKey, searchValue, pageable);
    }

    /**
     * Retrieves a wallet entity by its unique identifier.
     *
     * @param id the ID of the wallet to retrieve
     * @return the corresponding Wallet DTO
     */
    public WalletDto findById(Long id) {
        return WalletDto.fromWallet(walletQueryService.findById(id));
    }
}

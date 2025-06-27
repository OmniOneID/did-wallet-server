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
package org.omnione.did.wallet.v1.admin.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.db.domain.WalletServiceInfo;
import org.omnione.did.base.db.repository.WalletServiceRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.springframework.stereotype.Service;

/**
 * Service for querying and managing wallet service entity information.
 * <p>
 * Provides methods to retrieve or persist wallet service data, including handling exceptions.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WalletServiceQueryService {

    private final WalletServiceRepository walletServiceRepository;

    /**
     * Retrieves the first wallet service record ordered by ID.
     *
     * @return the WalletServiceInfo entity
     * @throws OpenDidException if the wallet service information is not found or an error occurs
     */
    public WalletServiceInfo findWalletService() {
        try {
            return walletServiceRepository.findTop1ByOrderByIdAsc()
                    .orElseThrow(() -> new OpenDidException(ErrorCode.WALLET_SERVICE_INFO_NOT_FOUND));
        } catch (OpenDidException e) {
            log.error("Wallet Service not found : {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while finding Wallet Service : {}", e.getMessage());
            throw new OpenDidException(ErrorCode.WALLET_SERVICE_INFO_NOT_FOUND);
        }
    }

    /**
     * Retrieves the first wallet service record ordered by ID, or null if not found.
     *
     * @return the WalletServiceInfo entity or null
     */
    public WalletServiceInfo findWalletServiceOrNull() {
        return walletServiceRepository.findTop1ByOrderByIdAsc().orElse(null);
    }

    /**
     * Persists the given wallet service entity.
     *
     * @param existedWalletService the wallet service to save
     * @return the saved WalletServiceInfo
     */
    public WalletServiceInfo save(WalletServiceInfo existedWalletService) {
        return walletServiceRepository.save(existedWalletService);
    }
}

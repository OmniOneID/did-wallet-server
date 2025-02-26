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

package org.omnione.did.wallet.v1.agent.service.query;

import lombok.RequiredArgsConstructor;
import org.omnione.did.base.db.domain.Wallet;
import org.omnione.did.base.db.repository.WalletRepository;
import org.springframework.stereotype.Service;

/**
 * The WalletQueryService class provides methods for saving and retrieving Wallet data.
 * It is designed to facilitate the storage and retrieval of Wallet data, ensuring that the data is accurate and up-to-date.
 */
@RequiredArgsConstructor
@Service
public class WalletQueryService {
    private final WalletRepository walletRepository;

    /**
     * Saves the given Wallet object in the database.
     *
     * @param wallet The Wallet object to be saved.
     * @return The saved Wallet object.
     */
    public Wallet save(Wallet wallet) {

        return walletRepository.save(wallet);
    }
}


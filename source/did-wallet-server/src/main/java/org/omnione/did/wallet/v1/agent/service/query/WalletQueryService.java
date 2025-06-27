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
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.wallet.v1.admin.dto.wallet.WalletDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public Page<WalletDto> searchWalletList(String searchKey, String searchValue, Pageable pageable) {
        Page<Wallet> walletPage = walletRepository.searchWallets(searchKey, searchValue, pageable);

        List<WalletDto> walletDtos = walletPage.getContent().stream()
                .map(WalletDto::fromWallet)
                .collect(Collectors.toList());

        return new PageImpl<>(walletDtos, pageable, walletPage.getTotalElements());
    }

    public Wallet findById(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new OpenDidException(ErrorCode.WALLET_INFO_NOT_FOUND));
    }
}


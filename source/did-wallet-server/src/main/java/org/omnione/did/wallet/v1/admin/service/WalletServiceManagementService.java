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
import org.omnione.did.base.db.constant.WalletServiceStatus;
import org.omnione.did.base.db.domain.WalletService;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.wallet.v1.admin.dto.admin.GetWalletServiceInfoReqDto;
import org.omnione.did.wallet.v1.admin.service.query.WalletServiceQueryService;
import org.omnione.did.wallet.v1.agent.service.StorageService;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class WalletServiceManagementService {
    private final WalletServiceQueryService walletServiceQueryService;
    private final StorageService storageService;

    public GetWalletServiceInfoReqDto getCasInfo() {
        WalletService existingCas = walletServiceQueryService.findWalletService();

        if (existingCas.getStatus() != WalletServiceStatus.ACTIVATE) {
            return GetWalletServiceInfoReqDto.fromEntity(existingCas);
        }

        DidDocument didDocument = storageService.findDidDoc(existingCas.getDid());
        return GetWalletServiceInfoReqDto.fromEntity(existingCas, didDocument);
    }
}

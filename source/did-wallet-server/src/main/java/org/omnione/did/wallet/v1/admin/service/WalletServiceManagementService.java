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
import org.omnione.did.base.db.constant.WalletServiceStatus;
import org.omnione.did.base.db.domain.CertificateVc;
import org.omnione.did.base.db.domain.WalletServiceInfo;
import org.omnione.did.base.db.repository.WalletServiceRepository;
import org.omnione.did.base.utils.BaseMultibaseUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.wallet.v1.admin.dto.walletservice.SendCertificateVcReqDto;
import org.omnione.did.wallet.v1.admin.dto.admin.GetWalletServiceInfoReqDto;
import org.omnione.did.wallet.v1.admin.dto.walletservice.SendEntityInfoReqDto;
import org.omnione.did.wallet.v1.admin.service.query.WalletServiceQueryService;
import org.omnione.did.wallet.v1.agent.service.StorageService;
import org.omnione.did.wallet.v1.agent.service.query.CertificateVcQueryService;
import org.omnione.did.wallet.v1.common.dto.EmptyResDto;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class WalletServiceManagementService {
    private final WalletServiceQueryService walletServiceQueryService;
    private final StorageService storageService;
    private final WalletServiceRepository walletServiceRepository;
    private final CertificateVcQueryService certificateVcQueryService;

    public GetWalletServiceInfoReqDto getCasInfo() {
        WalletServiceInfo existingWalletService = walletServiceQueryService.findWalletService();

        if (existingWalletService.getStatus() != WalletServiceStatus.ACTIVATE) {
            return GetWalletServiceInfoReqDto.fromEntity(existingWalletService);
        }

        DidDocument didDocument = storageService.findDidDoc(existingWalletService.getDid());
        return GetWalletServiceInfoReqDto.fromEntity(existingWalletService, didDocument);
    }

    public EmptyResDto createCertificateVc( SendCertificateVcReqDto sendCertificateVcReqDto) {
        byte[] decodedVc = BaseMultibaseUtil.decode(sendCertificateVcReqDto.getCertificateVc());
        log.debug("Decoded VC: {}", new String(decodedVc));

        certificateVcQueryService.save(CertificateVc.builder()
                .vc(new String(decodedVc))
                .build());

        return new EmptyResDto();
    }

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
}

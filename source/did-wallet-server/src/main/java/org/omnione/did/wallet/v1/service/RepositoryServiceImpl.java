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

package org.omnione.did.wallet.v1.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.utils.BaseCoreDidUtil;
import org.omnione.did.base.utils.BaseMultibaseUtil;
import org.omnione.did.common.util.DidUtil;
import org.omnione.did.core.manager.DidManager;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.wallet.v1.api.RepositoryFeign;
import org.omnione.did.wallet.v1.api.dto.DidDocApiResDto;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Implementation of the StorageService interface.
 * This service is used to retrieve DID documents from the repository.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Primary
@Profile("repository")
public class RepositoryServiceImpl implements StorageService {
    private final RepositoryFeign repositoryFeign;

    /**
     * Retrieves a DID document from the repository.
     *
     * @param didKeyUrl The DID key URL to retrieve the DID document.
     * @return The retrieved DID document.
     * @throws OpenDidException find DID document failure
     */
    @Override
    public DidDocument findDidDoc(String didKeyUrl) {
        try {
            String did = DidUtil.extractDid(didKeyUrl);

            DidDocApiResDto didDocApiResDto = repositoryFeign.getDid(did);

            byte[] decodedDidDoc = BaseMultibaseUtil.decode(didDocApiResDto.getDidDoc());

            String didDocJson = new String(decodedDidDoc);
            DidManager didManager = BaseCoreDidUtil.parseDidDoc(didDocJson);

            return didManager.getDocument();
        } catch (FeignException e) {
            log.error("Failed to find DID document.", e);
            throw new OpenDidException(ErrorCode.FIND_DID_DOC_FAILURE);
        } catch (Exception e) {
            log.error("Failed to find DID document.", e);
            throw new OpenDidException(ErrorCode.FIND_DID_DOC_FAILURE);
        }
    }
}

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
import org.omnione.did.base.db.domain.EntityDidDocument;
import org.omnione.did.base.db.repository.DidDocumentRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.springframework.stereotype.Service;

/**
 * Service for querying and managing DID Document records for the wallet service.
 * <p>
 * Provides methods to retrieve the latest DID Document or save a new one.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DidDocumentQueryService {

    private final DidDocumentRepository didDocumentRepository;

    /**
     * Retrieves the most recently saved DID Document.
     *
     * @return the latest EntityDidDocument
     * @throws OpenDidException if no DID Document is found
     */
    public EntityDidDocument findDidDocument() {
        return didDocumentRepository.findTop1ByOrderByIdDesc()
                .orElseThrow(() -> new OpenDidException(ErrorCode.WALLET_SERVICE_DID_DOCUMENT_NOT_FOUND));
    }

    /**
     * Retrieves the most recently saved DID Document or returns null if not found.
     *
     * @return the latest EntityDidDocument or null
     */
    public EntityDidDocument findDidDocumentOrNull() {
        return didDocumentRepository.findTop1ByOrderByIdDesc().orElse(null);
    }

    /**
     * Saves the provided DID Document entity.
     *
     * @param entityDidDocument the DID Document to save
     * @return the saved EntityDidDocument
     */
    public EntityDidDocument save(EntityDidDocument entityDidDocument) {
        return didDocumentRepository.save(entityDidDocument);
    }
}

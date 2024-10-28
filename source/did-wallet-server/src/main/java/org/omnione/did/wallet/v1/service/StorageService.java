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

import org.omnione.did.data.model.did.DidDocument;

/**
 * The StorageService interface defines the methods for finding DID documents.
 */
public interface StorageService {
    /**
     * Finds a DID document with the given DID key URL.
     *
     * @param didKeyUrl the DID key URL to search for
     * @return the corresponding DidDocument object
     */
    DidDocument findDidDoc(String didKeyUrl);
}

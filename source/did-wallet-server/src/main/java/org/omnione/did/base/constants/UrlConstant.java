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

package org.omnione.did.base.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Represents URL constants for the Open DID servers.
 * This class organizes all the API endpoints used in the application, ensuring that URLs are consistent and easy to manage.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlConstant {
    public static class Wallet {
        public static final String V1 = "/wallet/api/v1";
        public static final String REQUEST_SIGN_WALLET = "/request-sign-wallet";
    }
}

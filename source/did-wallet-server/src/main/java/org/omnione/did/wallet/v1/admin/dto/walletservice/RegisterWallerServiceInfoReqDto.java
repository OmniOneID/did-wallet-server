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
package org.omnione.did.wallet.v1.admin.dto.walletservice;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Data Transfer Object for registering wallet service information in the Admin Console.
 * <p>
 * Includes the service name and its endpoint URL.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RegisterWallerServiceInfoReqDto {
    @NotNull(message = "name cannot be null")
    private String name;

    @NotNull(message = "serverUrl cannot be null")
    private String serverUrl;
}

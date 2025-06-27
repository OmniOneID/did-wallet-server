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
import org.omnione.did.base.db.domain.Admin;
import org.omnione.did.wallet.v1.admin.dto.admin.AdminDto;
import org.omnione.did.wallet.v1.admin.dto.admin.RequestAdminLoginReqDto;
import org.omnione.did.wallet.v1.admin.service.query.AdminQueryService;
import org.springframework.stereotype.Service;

/**
 * Service for handling admin session logic in the Admin Console.
 * <p>
 * Provides login functionality by validating credentials and returning admin information.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SessionService {

    private final AdminQueryService adminQueryService;

    /**
     * Authenticates an admin using provided login credentials.
     *
     * @param requestAdminLoginReqDto login request containing login ID and password
     * @return authenticated admin information
     */
    public AdminDto requestAdminLogin(RequestAdminLoginReqDto requestAdminLoginReqDto) {
        Admin admin = adminQueryService.findByLoginIdAndLoginPassword(
                requestAdminLoginReqDto.getLoginId(),
                requestAdminLoginReqDto.getLoginPassword()
        );
        return AdminDto.fromAdmin(admin);
    }
}

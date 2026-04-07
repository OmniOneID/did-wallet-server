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
package org.omnione.did.wallet.v1.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.wallet.v1.admin.dto.admin.AdminPasswordPolicyDto;
import org.omnione.did.wallet.v1.admin.dto.admin.RegisterAdminPasswordPolicyReqDto;
import org.omnione.did.wallet.v1.admin.service.AdminPasswordPolicyManagementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing admin password policy settings in the Admin Console.
 * <p>
 * Provides endpoints to retrieve and update the system-wide password policy.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Admin.V1)
public class AdminPasswordPolicyManagementController {

    private final AdminPasswordPolicyManagementService adminPasswordPolicyManagementService;

    /**
     * Retrieves the current admin password policy.
     *
     * @return the current password policy DTO
     */
    @GetMapping(value = "/admin-password-policy")
    @ResponseBody
    public AdminPasswordPolicyDto getAdminPasswordPolicy() {
        return adminPasswordPolicyManagementService.getAdminPasswordPolicy();
    }

    /**
     * Creates or updates the admin password policy.
     *
     * @param reqDto the new policy settings
     * @return the updated policy DTO
     */
    @PostMapping(value = "/admin-password-policy")
    @ResponseBody
    public AdminPasswordPolicyDto registerAdminPasswordPolicy(
            @Valid @RequestBody RegisterAdminPasswordPolicyReqDto reqDto) {
        return adminPasswordPolicyManagementService.registerAdminPasswordPolicy(reqDto);
    }
}

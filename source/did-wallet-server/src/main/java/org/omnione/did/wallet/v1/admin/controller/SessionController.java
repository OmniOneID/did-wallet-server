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
import org.omnione.did.wallet.v1.admin.dto.admin.AdminDto;
import org.omnione.did.wallet.v1.admin.dto.admin.RequestAdminLoginReqDto;
import org.omnione.did.wallet.v1.admin.service.SessionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling admin login sessions in the Admin Console.
 * <p>
 * Provides an endpoint for authenticating admin credentials and initiating a session.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Admin.V1)
public class SessionController {

    private final SessionService sessionService;

    /**
     * Authenticates an admin and returns admin details upon successful login.
     *
     * @param requestAdminLoginReqDto DTO containing login ID and password
     * @return authenticated admin details
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public AdminDto requestAdminLogin(@Valid @RequestBody RequestAdminLoginReqDto requestAdminLoginReqDto) {
        return sessionService.requestAdminLogin(requestAdminLoginReqDto);
    }
}

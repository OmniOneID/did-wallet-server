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
import org.omnione.did.base.db.domain.Admin;
import org.omnione.did.base.db.repository.AdminRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.wallet.v1.admin.dto.admin.AdminDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for querying administrator information from the database.
 * <p>
 * Provides methods to find, search, and count admin users based on various criteria.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminQueryService {

    private final AdminRepository adminRepository;

    /**
     * Finds an admin by login ID and password.
     *
     * @param loginId the admin's login ID
     * @param loginPassword the admin's login password
     * @return the matched Admin entity
     * @throws OpenDidException if no matching admin is found
     */
    public Admin findByLoginIdAndLoginPassword(String loginId, String loginPassword) {
        return adminRepository.findByLoginIdAndLoginPassword(loginId, loginPassword)
                .orElseThrow(() -> new OpenDidException(ErrorCode.ADMIN_INFO_NOT_FOUND));
    }

    /**
     * Searches admins based on the given key and value with pagination.
     *
     * @param searchKey the field to search by
     * @param searchValue the value to match
     * @param pageable pagination configuration
     * @return a page of AdminDto results
     */
    public Page<AdminDto> searchAdminList(String searchKey, String searchValue, Pageable pageable) {
        Page<Admin> adminPage = adminRepository.searchAdmins(searchKey, searchValue, pageable);

        List<AdminDto> adminDtos = adminPage.getContent().stream()
                .map(AdminDto::fromAdmin)
                .collect(Collectors.toList());

        return new PageImpl<>(adminDtos, pageable, adminPage.getTotalElements());
    }

    /**
     * Finds an admin by ID.
     *
     * @param id the admin ID
     * @return the matched Admin entity
     * @throws OpenDidException if not found
     */
    public Admin findById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new OpenDidException(ErrorCode.ADMIN_INFO_NOT_FOUND));
    }

    /**
     * Finds an admin by login ID or returns null if not found.
     *
     * @param loginId the login ID
     * @return the matched Admin entity or null
     */
    public Admin findByLoginIdOrNull(String loginId) {
        return adminRepository.findByLoginId(loginId).orElse(null);
    }

    /**
     * Counts how many admins exist with the given login ID.
     *
     * @param loginId the login ID
     * @return number of matching admins
     */
    public long countByLoginId(String loginId) {
        return adminRepository.countByLoginId(loginId);
    }

    /**
     * Finds an admin by login ID.
     *
     * @param loginId the login ID
     * @return the matched Admin entity
     * @throws OpenDidException if not found
     */
    public Admin findByLoginId(String loginId) {
        return adminRepository.findByLoginId(loginId)
                .orElseThrow(() -> new OpenDidException(ErrorCode.ADMIN_INFO_NOT_FOUND));
    }
}

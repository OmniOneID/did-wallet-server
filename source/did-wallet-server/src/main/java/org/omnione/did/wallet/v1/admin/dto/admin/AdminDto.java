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
package org.omnione.did.wallet.v1.admin.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.base.db.constant.AdminRole;
import org.omnione.did.base.db.domain.Admin;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AdminDto {
    private final Long id;
    private final String loginId;
    private final String name;
    private final String email;
    private final Boolean emailVerified;
    private final Boolean requirePasswordReset;
    private final AdminRole role;
    private final String createdBy;
    private final String createdAt;
    private final String updatedAt;

    public static AdminDto fromAdmin(Admin admin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return AdminDto.builder()
                .id(admin.getId())
                .loginId(admin.getLoginId())
                .name(admin.getName())
                .emailVerified(admin.getEmailVerified())
                .requirePasswordReset(admin.getRequirePasswordReset())
                .role(admin.getRole())
                .createdBy(admin.getCreatedBy())
                .createdAt(formatInstant(admin.getCreatedAt(), formatter))
                .updatedAt(formatInstant(admin.getUpdatedAt(), formatter))
                .build();
    }

    private static String formatInstant(Instant instant, DateTimeFormatter formatter) {
        if (instant == null) return null;
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(formatter);
    }
}

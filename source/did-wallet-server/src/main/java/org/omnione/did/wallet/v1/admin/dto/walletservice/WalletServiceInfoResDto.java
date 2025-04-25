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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.omnione.did.base.db.constant.WalletServiceStatus;
import org.omnione.did.base.db.domain.WalletServiceInfo;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.data.model.did.DidDocument;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

/**
 * Data Transfer Object representing the response for wallet Entity registration.
 * <p>
 * Includes service metadata, DID, status, endpoints, and associated DID Document details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class WalletServiceInfoResDto {
    private Long id;
    private String did;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private WalletServiceStatus status;
    private String serverUrl;
    private String certificateUrl;
    private Map<String, Object> didDocument;
    private String createdAt;
    private String updatedAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
    private static String formatInstant(Instant instant) {
        return Optional.ofNullable(instant)
                .map(FORMATTER::format)
                .orElse(null);
    }

    public static WalletServiceInfoResDto fromEntity(WalletServiceInfo wallet) {
        return Optional.ofNullable(wallet)
                .map(t -> WalletServiceInfoResDto.builder()
                        .id(t.getId())
                        .did(t.getDid())
                        .name(t.getName())
                        .status(t.getStatus())
                        .serverUrl(t.getServerUrl())
                        .certificateUrl(t.getCertificateUrl())
                        .createdAt(formatInstant(t.getCreatedAt()))
                        .updatedAt(formatInstant(t.getUpdatedAt()))
                        .build())
                .orElse(null);
    }

    public static WalletServiceInfoResDto fromEntity(WalletServiceInfo wallet, DidDocument didDocument) {
        return Optional.ofNullable(wallet)
                .map(t -> WalletServiceInfoResDto.builder()
                        .id(t.getId())
                        .did(t.getDid())
                        .name(t.getName())
                        .status(t.getStatus())
                        .serverUrl(t.getServerUrl())
                        .certificateUrl(t.getCertificateUrl())
                        .didDocument(parseDidDocToMap(didDocument.toJson()))
                        .createdAt(formatInstant(t.getCreatedAt()))
                        .updatedAt(formatInstant(t.getUpdatedAt()))
                        .build())
                .orElse(null);
    }

    public static Map<String, Object> parseDidDocToMap(String didDocJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(didDocJson, Map.class);
        } catch (JsonProcessingException e) {
            throw new OpenDidException(ErrorCode.INVALID_DID_DOCUMENT);
        } catch (Exception e) {
            throw new OpenDidException(ErrorCode.INVALID_DID_DOCUMENT);
        }
    }
}

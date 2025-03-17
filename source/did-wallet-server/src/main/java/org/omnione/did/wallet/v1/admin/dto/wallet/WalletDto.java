package org.omnione.did.wallet.v1.admin.dto.wallet;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.omnione.did.base.db.domain.Wallet;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
public class WalletDto {
    private Long id;
    private String walletDid;
    private String walletId;
    private final String createdAt;
    private final String updatedAt;

    public static WalletDto fromWallet(Wallet wallet) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return WalletDto.builder()
                .id(wallet.getId())
                .walletDid(wallet.getWalletDid())
                .walletId(wallet.getWalletId())
                .createdAt(formatInstant(wallet.getCreatedAt(), formatter))
                .updatedAt(formatInstant(wallet.getUpdatedAt(), formatter))
                .build();
    }

    private static String formatInstant(Instant instant, DateTimeFormatter formatter) {
        if (instant == null) return null;
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(formatter);
    }
}

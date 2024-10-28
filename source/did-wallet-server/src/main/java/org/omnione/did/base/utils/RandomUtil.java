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

package org.omnione.did.base.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.omnione.did.common.util.HexUtil;
import org.omnione.did.common.util.NonceGenerator;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * Random Utility
 * This class provides methods to generate random values.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomUtil {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Generate a UUID.
     * @return UUID as a string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    /**
     * Generate a message ID.
     * @return Message ID as a string
     */
    public static String generateMessageId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateAndTime = dateFormat.format(new Date());

        long currentTimeMillis = System.currentTimeMillis();
        long microsecond = (currentTimeMillis % 1000) * 1000;

        String randomHexString = generateUUID()
                .replace("-", "")
                .substring(0, 8);

        return currentDateAndTime +
                String.format("%06d", microsecond) +
                randomHexString;
    }

    /**
     * Generate a wallet ID.
     * @return Wallet ID as a string
     */
    public static String generateWalletId() {
        String prefix = "WID";
        String datePart = getCurrentYearMonth();
        String randomPart = generateRandomString(11);


        return prefix + datePart + randomPart;
    }
    /**
     * Generate a wallet ID.
     * @return Wallet ID as a string
     */
    private static String getCurrentYearMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    /**
     * Generate a nonce.
     * @return Nonce as a string
     */
    // 주어진 길이의 무작위 문자열 생성하는 메소드
    public static String generateNonce() {
        return HexUtil.toHexString(NonceGenerator.generate16ByteNonce());
    }

    /**
     * Generate a random string.
     * @param length Length of the string
     * @return Random string
     */
    // 주어진 길이의 무작위 문자열을 생성하는 메소드
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}

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

package org.omnione.did.wallet.v1.agent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.property.WalletProperty;
import org.omnione.did.common.exception.CommonSdkException;
import org.omnione.did.common.util.wallet.BaseWalletUtil;
import org.omnione.did.wallet.exception.WalletException;
import org.omnione.did.wallet.key.WalletManagerInterface;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Service for handling file wallet operations.
 * This class provides methods for connecting to a file wallet and generating compact signatures.
 */
@Service
@Slf4j
public class FileWalletService {
    private final WalletProperty walletProperty;
    private final WalletManagerInterface walletManager;


    public FileWalletService(WalletProperty walletProperty) {
        this.walletProperty = walletProperty;
        try {
            this.walletManager = BaseWalletUtil.getFileWalletManager();
        } catch (CommonSdkException e) {
            throw new OpenDidException(ErrorCode.FAILED_TO_GET_FILE_WALLET_MANAGER);
        }
    }

    /**
     * Connects to the wallet using the wallet file path and password.
     *
     * @throws OpenDidException if the connection to the wallet fails
     */
    public void connectToWallet() {
        try {
            walletManager.connect(walletProperty.getFilePath(), walletProperty.getPassword().toCharArray());
        } catch (WalletException e) {
            log.error("Failed to connect to wallet: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.WALLET_CONNECTION_FAILED);
        }
    }

    /**
     * Generates a compact signature for the given plain text using the key with the given key ID.
     *
     * @param keyId     the key ID of the key to use for signing
     * @param plainText the plain text to sign
     * @return the compact signature as a byte array
     * @throws OpenDidException if the signature generation fails
     */
    public byte[] generateCompactSignature(String keyId, String plainText) {
        return generateCompactSignature(keyId, plainText.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a compact signature for the given plain text using the key with the given key ID.
     *
     * @param keyId     the key ID of the key to use for signing
     * @param plainText the plain text to sign
     * @return the compact signature as a byte array
     * @throws OpenDidException if the signature generation fails
     */
    public byte[] generateCompactSignature(String keyId, byte[] plainText) {
        try {
            if (!walletManager.isConnect()) {
                log.info("Wallet manager disConnect. Connecting to wallet...");
                connectToWallet();
            }

            byte[] signature = BaseWalletUtil.generateCompactSignature(walletManager, keyId, plainText);
            log.info("Compact signature generated for keyId: {}", keyId);
            return signature;
        } catch (OpenDidException e) {
            throw e;
        } catch (CommonSdkException e) {
            // The removed local util reported SIGNATURE_GENERATION_FAILED, not WALLET_SIGNATURE_GENERATION_FAILED
            throw new OpenDidException(ErrorCode.SIGNATURE_GENERATION_FAILED);
        } catch (Exception e) {
            throw new OpenDidException(ErrorCode.WALLET_SIGNATURE_GENERATION_FAILED);
        }
    }

    public WalletManagerInterface initializeWalletWithKeys() {
        try {
            return BaseWalletUtil.initializeWalletWithKeys(
                    walletProperty.getFilePath(),
                    walletProperty.getPassword(),
                    "auth", "assert", "keyagree", "invoke"
            );
        } catch (CommonSdkException e) {
            // Keep the error codes the wallet initialization returned before the Common SDK switch
            org.omnione.did.common.exception.ErrorCode code = e.getErrorCode();
            if (code == org.omnione.did.common.exception.ErrorCode.WALLET_ALREADY_EXISTS) {
                throw new OpenDidException(ErrorCode.WALLET_ALREADY_EXISTS);
            }
            if (code == org.omnione.did.common.exception.ErrorCode.KEY_ALREADY_EXISTS) {
                throw new OpenDidException(ErrorCode.CRYPTO_KEY_PAIR_ALREADY_EXISTS);
            }
            if (code == org.omnione.did.common.exception.ErrorCode.KEY_GENERATION_FAILED) {
                throw new OpenDidException(ErrorCode.KEY_PAIR_GENERATION_FAILED);
            }
            throw new OpenDidException(ErrorCode.WALLET_CONNECTION_FAILED);
        }
    }
}

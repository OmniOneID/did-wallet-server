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
import org.omnione.did.base.db.domain.CertificateVc;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.data.model.vc.VerifiableCredential;
import org.omnione.did.wallet.v1.agent.service.query.CertificateVcQueryService;
import org.springframework.stereotype.Service;

/**
 * This service is used to retrieve a certificate VC from the database.
 * The service retrieves the certificate VC from the database and returns it as a VerifiableCredential object.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CertificateVcServiceImpl implements CertificateVcService {
    private final CertificateVcQueryService certificateVcQueryService;
    /**
     * Request a certificate VC from the database.
     *
     * @return the certificate VC as a String
     * @throws OpenDidException if an error occurs during the retrieval process
     */
    @Override
    public String requestCertificateVc() {
        try {
            CertificateVc certificateVc = certificateVcQueryService.findCertificateVc();
            VerifiableCredential verifiableCredential = new VerifiableCredential();
            verifiableCredential.fromJson(certificateVc.getVc());

            return verifiableCredential.toJson();
        } catch (OpenDidException e) {
            log.error("An OpenDidException occurred while sending requestCertificateVc request", e);
            throw e;
        } catch (Exception e) {
            log.error("An unknown error occurred while sending requestCertificateVc request", e);
            throw new OpenDidException(ErrorCode.TR_GET_CERTIFICATE_VC_FAILED);
        }
    }

}

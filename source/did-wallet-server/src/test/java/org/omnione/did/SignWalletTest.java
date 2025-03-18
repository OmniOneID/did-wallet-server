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

package org.omnione.did;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.base.utils.BaseCoreDidUtil;
import org.omnione.did.core.manager.DidManager;
import org.omnione.did.data.model.did.DidDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = WalletApplication.class)
@ActiveProfiles("sample")
class SignWalletTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Sign Wallet Test")
    void testSignWallet() throws Exception {
        // 1. 요청 DTO 설정
        String didDocJson = """
                {"@context":["https://www.w3.org/ns/did/v1"],"assertionMethod":["assert"],"authentication":["auth"],"controller":"did:omn:tas","created":"2024-09-05T08:16:57Z","deactivated":false,"id":"did:omn:2K8iHSTyZECsQx3FWUE53gBc6Xr5","keyAgreement":["keyagree"],"proofs":[{"created":"2024-09-05T08:16:57Z","proofPurpose":"assertionMethod","proofValue":"mIIRwTyGNmoyyX85YKNXuPDJD2Fa9ftKdIDF1WH98iJzQSK9NPCoWxCUc8hvKNOnfWUmOvoJRa+Hu+1UXhQK/TDg=","type":"Secp256r1Signature2018","verificationMethod":"did:omn:2K8iHSTyZECsQx3FWUE53gBc6Xr5?versionId=1#assert"},{"created":"2024-09-05T08:16:57Z","proofPurpose":"authentication","proofValue":"mIK1IgIhez27Onuei4512ho2oY+Z2TLV9JoRexqknR2ToUIIgEdHJa2gIfElaaBUSEJKTqXETcHFMLzn8gjqM2q8=","type":"Secp256r1Signature2018","verificationMethod":"did:omn:2K8iHSTyZECsQx3FWUE53gBc6Xr5?versionId=1#auth"}],"updated":"2024-09-05T08:16:57Z","verificationMethod":[{"authType":1,"controller":"did:omn:tas","id":"keyagree","publicKeyMultibase":"mA7RK+/86wDvopTd4hyd08lQuLrxOXZUASNAdA+JUg+eT","type":"Secp256r1VerificationKey2018"},{"authType":1,"controller":"did:omn:tas","id":"auth","publicKeyMultibase":"mAqmLQlpWKJSHXbRwPNl2vHVXlLVedF43qZv9FK49epZR","type":"Secp256r1VerificationKey2018"},{"authType":1,"controller":"did:omn:tas","id":"assert","publicKeyMultibase":"mAzVzjaLzpaMfP3UG2Y4ngxufk2ODRl3N3Gmwg4JqBZ9U","type":"Secp256r1VerificationKey2018"}],"versionId":"1"}
                """;

        DidManager didManager = BaseCoreDidUtil.parseDidDoc(didDocJson);

        // 2. 요청
        MvcResult result = mockMvc.perform(post(UrlConstant.Wallet.AGENT_V1 + UrlConstant.Wallet.REQUEST_SIGN_WALLET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(didManager.getDocument())))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // 3. 실제 응답 확인
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Actual Controller Response: " + responseBody);
    }
}

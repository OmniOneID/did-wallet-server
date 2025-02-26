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

package org.omnione.did.wallet.v1.agent.service.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.datamodel.data.AttestedDidDoc;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.wallet.v1.agent.service.WalletService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * sample WalletService implementation
 * this class is used for testing purposes only
 */
@Profile("sample")
@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceSample implements WalletService {

    /**
     * Signs a wallet by creating an attestation proof and returning the attested DID document.
     *
     * @param request The did document representing the wallet.
     * @return The attested DID document with the wallet ID, owner DID document, provider information, nonce, and proof.
     */
    @Override
    public AttestedDidDoc signWallet(DidDocument request) {
        ObjectMapper objectMapper = new ObjectMapper();

        String attestedDidDocJson = """
                {"walletId":"WID202409bRzlIZtOmPM","ownerDidDoc":"ueyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvbnMvZGlkL3YxIl0sImFzc2VydGlvbk1ldGhvZCI6WyJhc3NlcnQiXSwiYXV0aGVudGljYXRpb24iOlsiYXV0aCJdLCJjb250cm9sbGVyIjoiZGlkOm9tbjp0YXMiLCJjcmVhdGVkIjoiMjAyNC0wOS0wNVQwODoxNjo1N1oiLCJkZWFjdGl2YXRlZCI6ZmFsc2UsImlkIjoiZGlkOm9tbjoySzhpSFNUeVpFQ3NReDNGV1VFNTNnQmM2WHI1Iiwia2V5QWdyZWVtZW50IjpbImtleWFncmVlIl0sInByb29mcyI6W3siY3JlYXRlZCI6IjIwMjQtMDktMDVUMDg6MTY6NTdaIiwicHJvb2ZQdXJwb3NlIjoiYXNzZXJ0aW9uTWV0aG9kIiwicHJvb2ZWYWx1ZSI6Im1JSVJ3VHlHTm1veXlYODVZS05YdVBESkQyRmE5ZnRLZElERjFXSDk4aUp6UVNLOU5QQ29XeENVYzhodktOT25mV1VtT3ZvSlJhK0h1KzFVWGhRSy9URGc9IiwidHlwZSI6IlNlY3AyNTZyMVNpZ25hdHVyZTIwMTgiLCJ2ZXJpZmljYXRpb25NZXRob2QiOiJkaWQ6b21uOjJLOGlIU1R5WkVDc1F4M0ZXVUU1M2dCYzZYcjU_dmVyc2lvbklkPTEjYXNzZXJ0In0seyJjcmVhdGVkIjoiMjAyNC0wOS0wNVQwODoxNjo1N1oiLCJwcm9vZlB1cnBvc2UiOiJhdXRoZW50aWNhdGlvbiIsInByb29mVmFsdWUiOiJtSUsxSWdJaGV6MjdPbnVlaTQ1MTJobzJvWStaMlRMVjlKb1JleHFrblIyVG9VSUlnRWRISmEyZ0lmRWxhYUJVU0VKS1RxWEVUY0hGTUx6bjhnanFNMnE4PSIsInR5cGUiOiJTZWNwMjU2cjFTaWduYXR1cmUyMDE4IiwidmVyaWZpY2F0aW9uTWV0aG9kIjoiZGlkOm9tbjoySzhpSFNUeVpFQ3NReDNGV1VFNTNnQmM2WHI1P3ZlcnNpb25JZD0xI2F1dGgifV0sInVwZGF0ZWQiOiIyMDI0LTA5LTA1VDA4OjE2OjU3WiIsInZlcmlmaWNhdGlvbk1ldGhvZCI6W3siYXV0aFR5cGUiOjEsImNvbnRyb2xsZXIiOiJkaWQ6b21uOnRhcyIsImlkIjoia2V5YWdyZWUiLCJwdWJsaWNLZXlNdWx0aWJhc2UiOiJtQTdSSysvODZ3RHZvcFRkNGh5ZDA4bFF1THJ4T1haVUFTTkFkQStKVWcrZVQiLCJ0eXBlIjoiU2VjcDI1NnIxVmVyaWZpY2F0aW9uS2V5MjAxOCJ9LHsiYXV0aFR5cGUiOjEsImNvbnRyb2xsZXIiOiJkaWQ6b21uOnRhcyIsImlkIjoiYXV0aCIsInB1YmxpY0tleU11bHRpYmFzZSI6Im1BcW1MUWxwV0tKU0hYYlJ3UE5sMnZIVlhsTFZlZEY0M3FadjlGSzQ5ZXBaUiIsInR5cGUiOiJTZWNwMjU2cjFWZXJpZmljYXRpb25LZXkyMDE4In0seyJhdXRoVHlwZSI6MSwiY29udHJvbGxlciI6ImRpZDpvbW46dGFzIiwiaWQiOiJhc3NlcnQiLCJwdWJsaWNLZXlNdWx0aWJhc2UiOiJtQXpWemphTHpwYU1mUDNVRzJZNG5neHVmazJPRFJsM04zR213ZzRKcUJaOVUiLCJ0eXBlIjoiU2VjcDI1NnIxVmVyaWZpY2F0aW9uS2V5MjAxOCJ9XSwidmVyc2lvbklkIjoiMSJ9","provider":{"did":"did:omn:wallet","certVcRef":"http://192.168.3.130:8095/wallet/api/v1/certificate-vc"},"nonce":"9d47acc73663e9ab436c94d31be32b62","proof":{"type":"Secp256r1Signature2018","created":"2024-09-05T17:16:56.769513Z","verificationMethod":"did:omn:wallet?versionId=1#assert","proofPurpose":"assertionMethod","proofValue":"mH7lqBPWwzQvQ2Iam2oIC6c95ntPHqCFMXorFhFJO0yPSXZaHt+OxyNktc73qw6rUdIy7t2/dfHnYXeePdEv69TE"},"did":"did:omn:wallet"}
                """;
        try {
            return objectMapper.readValue(attestedDidDocJson, AttestedDidDoc.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}

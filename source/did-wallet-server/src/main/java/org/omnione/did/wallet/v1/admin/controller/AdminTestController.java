package org.omnione.did.wallet.v1.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.base.db.domain.CertificateVc;
import org.omnione.did.base.utils.BaseMultibaseUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.wallet.v1.agent.service.StorageService;
import org.omnione.did.wallet.v1.agent.service.query.CertificateVcQueryService;
import org.omnione.did.wallet.v1.common.dto.EmptyResDto;
import org.omnione.did.wallet.v1.admin.dto.SendCertificateVcReqDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Wallet.ADMIN_V1)
public class AdminTestController {

    private final CertificateVcQueryService certificateVcQueryService;
    private final StorageService storageService;

    /* For testing purposes */
    @RequestMapping(value = "/certificate-vc", method = RequestMethod.POST)
    public EmptyResDto getCertificateVc(@RequestBody SendCertificateVcReqDto sendCertificateVcReqDto) {
        byte[] decodedVc = BaseMultibaseUtil.decode(sendCertificateVcReqDto.getCertificateVc());
        log.debug("Decoded VC: {}", new String(decodedVc));

        certificateVcQueryService.save(CertificateVc.builder()
                .vc(new String(decodedVc))
                .build());

        return new EmptyResDto();
    }

    @RequestMapping(value = "/did", method = RequestMethod.GET)
    public DidDocument testGetDid(@RequestParam String did) {

        log.debug("did: {}", did);
        DidDocument didDoc = storageService.findDidDoc(did);
        return didDoc;
    }
}


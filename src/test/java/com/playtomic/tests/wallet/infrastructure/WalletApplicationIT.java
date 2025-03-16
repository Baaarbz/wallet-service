package com.playtomic.tests.wallet.infrastructure;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureWireMock(port = 9999)
public class WalletApplicationIT {
}

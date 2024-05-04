/**
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package kz.core247.keycloak.eds.kalkan;

import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Provider;
import java.security.Security;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests SecurityProviderInitializer's initKalkanProvider method.
 */
public final class SecurityProviderInitializerTest {

    private static final Logger logger = LoggerFactory.getLogger(SecurityProviderInitializerTest.class);

    /**
     * Tests that initKalkanProvider adds the KalkanProvider to Security providers exactly once.
     */
    @Test
    public void initKalkanProvider_AddsKalkanProviderOnce() {
        SecurityProviderInitializer.initKalkanProvider();

        logger.info("Checking if KalkanProvider is added to Security providers..");
        assertSecurityProvidersContains(KalkanProvider.class);

        logger.info("Initialized again to check if KalkanProvider is added twice..");
        SecurityProviderInitializer.initKalkanProvider();
        
        assertSecurityProvidersContainsExactlyOne(KalkanProvider.class);
    }

    /**
     * Asserts that the Security providers contains a Provider of the given class.
     */
    private void assertSecurityProvidersContains(Class<? extends Provider> providerClass) {
        assertTrue(Arrays.asList(Security.getProviders()).stream()
                .anyMatch(provider -> provider.getClass().equals(providerClass)),
                "Security providers should contain a Provider of class " + providerClass.getName());
    }

    /**
     * Asserts that the Security providers contains exactly one Provider of the given class.
     */
    private void assertSecurityProvidersContainsExactlyOne(Class<? extends Provider> providerClass) {
        long count = Arrays.asList(Security.getProviders()).stream()
                .filter(provider -> provider.getClass().equals(providerClass))
                .count();

        assertEquals(1, count, "Security providers should contain exactly one Provider of class " + providerClass.getName());
    }
}
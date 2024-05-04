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
import kz.gov.pki.kalkan.xmldsig.KncaXS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Security;

/**
 * The SecurityProviderInitializer class provides a mechanism for initializing the Kalkan security provider and adding it to the Security providers if it has not been added already
 * .
 */
public final class SecurityProviderInitializer {

    private SecurityProviderInitializer() {
    }

    private static final Logger logger = LoggerFactory.getLogger(SecurityProviderInitializer.class);

    private static volatile boolean isAddedKalkan = false;

    /**
     * Initializes the Kalkan security provider and adds it to the Security providers if it has not been added already.
     * This method is thread-safe.
     */
    public static void initKalkanProvider() {
        if (!isAddedKalkan) {
            synchronized (SecurityProviderInitializer.class) {
                if (!isAddedKalkan) {
                    logger.info("Trying to add KalkanProvider to Security providers..");
                    Security.addProvider(new KalkanProvider());
                    KncaXS.loadXMLSecurity();
                    logger.info("Successfully added KalkanProvider to Security providers..");
                    isAddedKalkan = true;
                }
            }
        }
    }
}

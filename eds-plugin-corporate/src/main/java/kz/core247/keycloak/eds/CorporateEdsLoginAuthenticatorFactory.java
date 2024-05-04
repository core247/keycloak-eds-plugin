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
package kz.core247.keycloak.eds;

import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CorporateEdsLoginAuthenticatorFactory class is responsible for creating instances of the CorporateEdsLoginAuthenticator.
 * It extends the BaseEdsLoginAuthenticatorFactory class and implements the AuthenticatorFactory interface.
 */
public class CorporateEdsLoginAuthenticatorFactory extends BaseEdsLoginAuthenticatorFactory {

    private static final Logger logger = LoggerFactory.getLogger(CorporateEdsLoginAuthenticatorFactory.class);

    private static final CorporateEdsLoginAuthenticator SINGLETON = new CorporateEdsLoginAuthenticator();

    public static final String ID = "keycloak-corporate-eds-login";

    @Override
    public Authenticator create(KeycloakSession session) {
        logger.info("Trying to create {} via factory.", this.getClass().getSimpleName());
        return SINGLETON;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getReferenceCategory() {
        return "CorporateEdsLogin";
    }

    @Override
    public String getDisplayType() {
        return "Corporate EDS Login";
    }

    @Override
    public String getHelpText() {
        return "Corporate EDS Login";
    }
}

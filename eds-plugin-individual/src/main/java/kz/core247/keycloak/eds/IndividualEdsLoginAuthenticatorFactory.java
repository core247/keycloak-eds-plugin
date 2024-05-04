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
 * The IndividualEdsLoginAuthenticatorFactory class is responsible for creating instances of the IndividualEdsLoginAuthenticator class.
 * It extends the BaseEdsLoginAuthenticatorFactory class and implements the AuthenticatorFactory interface.
 */
public class IndividualEdsLoginAuthenticatorFactory extends BaseEdsLoginAuthenticatorFactory {

    private static final Logger logger = LoggerFactory.getLogger(IndividualEdsLoginAuthenticatorFactory.class);

    private static final IndividualEdsLoginAuthenticator SINGLETON = new IndividualEdsLoginAuthenticator();

    public static final String ID = "keycloak-individual-eds-login";

    /**
     * Creates an instance of the IndividualEdsLoginAuthenticator.
     *
     * @param session the KeycloakSession object used to create the authenticator
     * @return an Authenticator object representing the IndividualEdsLoginAuthenticator
     */
    @Override
    public Authenticator create(KeycloakSession session) {
        logger.info("Trying to create {} via factory.", this.getClass().getSimpleName());
        return SINGLETON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReferenceCategory() {
        return "IndividualEdsLogin";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayType() {
        return "Individual EDS Login";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelpText() {
        return "Individual EDS Login";
    }
}

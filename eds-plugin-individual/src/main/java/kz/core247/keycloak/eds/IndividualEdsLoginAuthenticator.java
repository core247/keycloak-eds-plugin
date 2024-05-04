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

import kz.core247.keycloak.eds.kalkan.CertificateMetadata;
import org.keycloak.models.UserModel;


/**
 * The IndividualEdsLoginAuthenticator class is responsible for authenticating individual users using EDS certificates.
 * It extends the EdsLoginAuthenticator class.
 *
 * <p>Here is an example of how to use the IndividualEdsLoginAuthenticator class:
 * <pre>{@code
 *     IndividualEdsLoginAuthenticator authenticator = new IndividualEdsLoginAuthenticator();
 *     AuthenticationFlowContext context = new AuthenticationFlowContext();
 *     authenticator.authenticate(context);
 * }</pre>
 */
public class IndividualEdsLoginAuthenticator extends EdsLoginAuthenticator {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(UserModel user, CertificateMetadata certificate) {
        return IndividualUserVerifierImpl.verify(user, certificate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isIndividual() {
        return true;
    }
}

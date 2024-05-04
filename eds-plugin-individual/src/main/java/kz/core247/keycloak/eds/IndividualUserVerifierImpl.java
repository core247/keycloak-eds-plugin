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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * The IndividualUserVerifierImpl class provides a method for verifying an individual user based on the tax code.
 */
public class IndividualUserVerifierImpl {

    private static final Logger logger = LoggerFactory.getLogger(IndividualUserVerifierImpl.class);

    /**
     * Verifies if the tax code in the given UserModel matches the tax code in the CertificateMetadata.
     *
     * @param currentUser   the UserModel representing the current user
     * @param edsMetadata   the CertificateMetadata containing the tax code to be verified
     * @return true if the tax codes match, false otherwise
     */
    public static boolean verify(UserModel currentUser, CertificateMetadata edsMetadata) {
        logger.info("Trying to match via username and taxcode.");
        Map<String, List<String>> attrs = currentUser.getAttributes();
        String taxCode = edsMetadata.getTaxCode();
        if (null == taxCode) {
            logger.warn("Input taxCode {} is null", taxCode);
            return false;
        }
        if (attrs != null && !attrs.isEmpty()) {
            List<String> taxCodeValues = attrs.get(KeycloakAttributes.TAX_CODE);
            logger.info("Trying to match taxCode {} in values {}", taxCode, taxCodeValues);
            boolean isValidTaxCode = null == taxCodeValues ? false : taxCodeValues.contains(taxCode);
            if (isValidTaxCode) {
                return true;
            }
            logger.info("Not matched by this verifier.");
        }
        return false;
    }
}

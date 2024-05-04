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

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Implementation class for matching a user based on taxCode and bin attributes.
 * This class extends the base UserVerifyMatcher class.
 */
public class CorporateUserVerifierImpl {
    private CorporateUserVerifierImpl() {
    }

    private static final Logger logger = LoggerFactory.getLogger(CorporateUserVerifierImpl.class);

    /**
     * Verifies if the current user matches the provided bin and taxCode attributes.
     *
     * @param currentUser The UserModel representing the current user.
     * @param edsMetadata The CertificateMetadata containing the bin and taxCode attributes to be verified.
     * @return true if the currentUser's attributes match the bin and taxCode attributes in edsMetadata, false otherwise.
     */
    public static boolean verify(@Nonnull UserModel currentUser, @Nonnull CertificateMetadata edsMetadata) {
        logger.info("Trying to match via user attributes and bin & taxCode...");
        Map<String, List<String>> attrs = currentUser.getAttributes();
        String bin = edsMetadata.getBin(), taxCode = edsMetadata.getTaxCode();
        if (null == bin && null == taxCode) {
            logger.warn("Input bin {} or taxCode {} is null", bin, taxCode);
            return false;
        }
        if (attrs != null && !attrs.isEmpty()) {
            List<String> taxCodeValues = attrs.get(KeycloakAttributes.TAX_CODE);
            List<String> binValues = attrs.get(KeycloakAttributes.BIN);
            logger.info("Trying to match taxCode {} in values {}," +
                    " bin {} in values {}", taxCode, taxCodeValues, bin, binValues);
            boolean isValidTaxCode = null == taxCodeValues ? false : taxCodeValues.contains(taxCode);
            boolean isValidBin = null == binValues ? false : binValues.contains(bin);
            if (isValidBin && isValidTaxCode) {
                return true;
            }
            logger.info("Not matched by this verifier.");
        }
        return false;
    }
}

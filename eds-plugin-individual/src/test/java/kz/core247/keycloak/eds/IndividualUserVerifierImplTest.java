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
import org.junit.jupiter.api.Test;
import org.keycloak.models.UserModel;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * This class provides unit tests for the "verify" method of the "IndividualUserVerifierImpl" class.
 * Note: The "verify" method checks if a user matches the provided certificate metadata based on the username and tax code.
 */
public class IndividualUserVerifierImplTest {

    @Test
    public void testVerifyWhenTaxCodeIsNull() {
        // Creating a mocked user model
        UserModel currentUser = Mockito.mock(UserModel.class);

        // Creating certificate metadata with null tax code
        CertificateMetadata edsMetadata = new CertificateMetadata().withTaxCode(null);

        // Test "verify" method
        boolean isValid = IndividualUserVerifierImpl.verify(currentUser, edsMetadata);

        assertEquals(false, isValid, "Expected verification to be false when tax code is null");
    }

    @Test
    public void testVerifyWhenAttributesAreEmpty() {
        // Creating a mocked user model
        UserModel currentUser = Mockito.mock(UserModel.class);

        // Creating certificate metadata
        CertificateMetadata edsMetadata = new CertificateMetadata().withTaxCode("123456789");

        when(currentUser.getAttributes()).thenReturn(new HashMap<>());
        
        // Test "verify" method
        boolean isValid = IndividualUserVerifierImpl.verify(currentUser, edsMetadata);

        assertEquals(false, isValid, "Expected verification to be false when user attributes are empty");
    }

    @Test
    public void testVerifyWhenTaxCodeIsNotInAttributes() {
        // Creating a mocked user model
        UserModel currentUser = Mockito.mock(UserModel.class);

        // Creating certificate metadata
        CertificateMetadata edsMetadata = new CertificateMetadata().withTaxCode("123456789");

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put(KeycloakAttributes.TAX_CODE, Arrays.asList("987654321"));
        
        when(currentUser.getAttributes()).thenReturn(attributes);

        // Test "verify" method
        boolean isValid = IndividualUserVerifierImpl.verify(currentUser, edsMetadata);

        assertEquals(false, isValid, "Expected verification to be false when tax code is not in user attributes");
    }

    @Test
    public void testVerifyWhenTaxCodeIsValid() {
        // Creating a mocked user model
        UserModel currentUser = Mockito.mock(UserModel.class);

        // Creating certificate metadata
        CertificateMetadata edsMetadata = new CertificateMetadata().withTaxCode("123456789");

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put(KeycloakAttributes.TAX_CODE, Arrays.asList("123456789"));
        
        when(currentUser.getAttributes()).thenReturn(attributes);

        // Test "verify" method
        boolean isValid = IndividualUserVerifierImpl.verify(currentUser, edsMetadata);

        assertEquals(true, isValid, "Expected verification to be true when tax code is valid");
    }

}
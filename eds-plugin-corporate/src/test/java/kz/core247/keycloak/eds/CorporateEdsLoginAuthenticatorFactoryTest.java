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

import org.junit.Test;
import org.keycloak.models.KeycloakSession;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CorporateEdsLoginAuthenticatorFactoryTest {

    @Test
    public void testCreate() {
        //GIVEN
        CorporateEdsLoginAuthenticatorFactory factory = new CorporateEdsLoginAuthenticatorFactory();

        // Mock the KeycloakSession
        KeycloakSession session = Mockito.mock(KeycloakSession.class);

        //WHEN
        var authenticator = factory.create(session);

        //THEN
        assertNotNull("Created Authenticator should not be null", authenticator);
        assertEquals("Class of the created authenticator should be CorporateEdsLoginAuthenticator",
                CorporateEdsLoginAuthenticator.class, authenticator.getClass());
    }

    @Test
    public void testGetID() {
        //GIVEN
        CorporateEdsLoginAuthenticatorFactory factory = new CorporateEdsLoginAuthenticatorFactory();

        //WHEN
        String id = factory.getId();

        //THEN
        assertEquals("The ID should be keycloak-corporate-eds-login", "keycloak-corporate-eds-login", id);
    }

    @Test
    public void testGetReferenceCategory() {
        //GIVEN
        CorporateEdsLoginAuthenticatorFactory factory = new CorporateEdsLoginAuthenticatorFactory();

        //WHEN
        String category = factory.getReferenceCategory();

        //THEN
        assertEquals("The category should be CorporateEdsLogin", "CorporateEdsLogin", category);
    }

    @Test
    public void testGetDisplayType() {
        //GIVEN
        CorporateEdsLoginAuthenticatorFactory factory = new CorporateEdsLoginAuthenticatorFactory();

        //WHEN
        String type = factory.getDisplayType();

        //THEN
        assertEquals("The display type should be Corporate EDS Login", "Corporate EDS Login", type);
    }

    @Test
    public void testGetHelpText() {
        //GIVEN
        CorporateEdsLoginAuthenticatorFactory factory = new CorporateEdsLoginAuthenticatorFactory();

        //WHEN
        String type = factory.getHelpText();

        //THEN
        assertEquals("Help text should be Corporate EDS Login", "Corporate EDS Login", type);
    }
}
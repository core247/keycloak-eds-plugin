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

import org.junit.jupiter.api.Test;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class IndividualEdsLoginAuthenticatorFactoryTest {

    private final IndividualEdsLoginAuthenticatorFactory factory = new IndividualEdsLoginAuthenticatorFactory();

    @Test
    public void testCreateMethod() {
        KeycloakSession session = mock(KeycloakSession.class);
        Authenticator authenticator = factory.create(session);
        assertTrue(authenticator instanceof IndividualEdsLoginAuthenticator, "The created Authenticator is not an instance of IndividualEdsLoginAuthenticator.");
    }

    @Test
    public void testGetIdMethod() {
        assertEquals("keycloak-individual-eds-login", factory.getId(), "The ID does not match the expected.");
    }
    
    @Test
    public void testGetReferenceCategory() {
        assertEquals("IndividualEdsLogin", factory.getReferenceCategory(), "The Reference Category does not match the expected.");
    }
    
    @Test
    public void testGetDisplayType() {
        assertEquals("Individual EDS Login", factory.getDisplayType(), "The Display Type does not match the expected.");
    }
    
    @Test
    public void testGetHelpText() {
        assertEquals("Individual EDS Login", factory.getHelpText(), "The Help Text does not match the expected.");
    }
}
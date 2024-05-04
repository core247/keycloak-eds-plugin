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

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import kz.core247.keycloak.eds.kalkan.CertificateMetadata;
import kz.core247.keycloak.eds.kalkan.CertificateParser;
import kz.core247.keycloak.eds.kalkan.LibCertificateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.UserModel;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CorporateEdsLoginAuthenticatorTest {

    @Test
    public void testIsIndividual() {
        CorporateEdsLoginAuthenticator authenticator = new CorporateEdsLoginAuthenticator();
        assertThat(authenticator.isIndividual()).isFalse();
    }

    @Test
    public void testCertificateValidationFailure() {
        CorporateEdsLoginAuthenticator authenticator = new CorporateEdsLoginAuthenticator();
        UserModel mockUserModel = Mockito.mock(UserModel.class);
        AuthenticationFlowContext mockContext = Mockito.mock(AuthenticationFlowContext.class);

        LoginFormsProvider mockForm = Mockito.mock(LoginFormsProvider.class);
        AuthenticationSessionModel authenticationSessionModel = Mockito.mock(AuthenticationSessionModel.class);
        lenient().when(mockContext.getAuthenticationSession()).thenReturn(authenticationSessionModel);

        lenient().when(mockUserModel.isEnabled()).thenReturn(true);
        lenient().when(authenticationSessionModel.getAuthenticatedUser()).thenReturn(mockUserModel);
        when(mockContext.form()).thenReturn(mockForm);
        when(mockForm.createErrorPage(Response.Status.UNAUTHORIZED)).thenReturn(Response.status(Response.Status.UNAUTHORIZED).build());

        HttpRequest httpRequest = Mockito.mock(HttpRequest.class);
        Mockito.when(mockContext.getHttpRequest()).thenReturn(httpRequest);
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("certificate", "invalid certificate");
        when(httpRequest.getDecodedFormParameters()).thenReturn(formData);

        Mockito.when(mockForm.setError((any(String.class)))).thenReturn(mockForm);

        authenticator.action(mockContext);

        assertEquals(authenticator.requiresUser(), false);
    }

    @Test
    public void testActionInvalidCertificate() {
        CorporateEdsLoginAuthenticator authenticator = new CorporateEdsLoginAuthenticator();
        AuthenticationFlowContext mockContext = Mockito.mock(AuthenticationFlowContext.class);

        HttpRequest mockHttpRequest = Mockito.mock(HttpRequest.class);
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("certificate", "invalid certificate");
        when(mockHttpRequest.getDecodedFormParameters()).thenReturn(formData);
        lenient().when(mockContext.getHttpRequest()).thenReturn(mockHttpRequest);

        LoginFormsProvider mockForm = Mockito.mock(LoginFormsProvider.class);
        when(mockContext.form()).thenReturn(mockForm);
        when(mockForm.createErrorPage(Response.Status.UNAUTHORIZED)).thenReturn(Response.status(Response.Status.UNAUTHORIZED).build());
        Mockito.when(mockForm.setError((any(String.class)))).thenReturn(mockForm);

        authenticator.action(mockContext);

        Mockito.verify(mockForm).setError(any(String.class));
        Mockito.verify(mockContext).failureChallenge(eq(AuthenticationFlowError.INVALID_CREDENTIALS), any(Response.class));
    }

    @Test
    public void testActionValidCertificate() throws LibCertificateException {
        // given
        try (MockedStatic<CertificateParser> mock = Mockito.mockStatic(CertificateParser.class);
            MockedStatic<CorporateUserVerifierImpl> mockVerifier = Mockito.mockStatic(CorporateUserVerifierImpl.class)) {
            CorporateEdsLoginAuthenticator authenticator = new CorporateEdsLoginAuthenticator();
            AuthenticationFlowContext mockContext = Mockito.mock(AuthenticationFlowContext.class);
            UserModel mockUser = Mockito.mock(UserModel.class);
            AuthenticationSessionModel mockAuthSession = Mockito.mock(AuthenticationSessionModel.class);

            lenient().when(mockContext.getAuthenticationSession()).thenReturn(mockAuthSession);
            lenient().when(mockAuthSession.getAuthenticatedUser()).thenReturn(mockUser);
            lenient().when(mockUser.isEnabled()).thenReturn(true);

            HttpRequest mockHttpRequest = Mockito.mock(HttpRequest.class);
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            formData.add("certificate", "valid certificate");
            when(mockHttpRequest.getDecodedFormParameters()).thenReturn(formData);
            lenient().when(mockContext.getHttpRequest()).thenReturn(mockHttpRequest);

            LoginFormsProvider mockForm = Mockito.mock(LoginFormsProvider.class);

            CertificateMetadata mockCertificate = Mockito.mock(CertificateMetadata.class);
            mock.when(() -> CertificateParser.parseMetadata("valid certificate", false)).thenReturn(mockCertificate);
            mockVerifier.when(() -> CorporateUserVerifierImpl.verify(mockUser, mockCertificate)).thenReturn(true);
            // when
            authenticator.action(mockContext);

            // then
            Mockito.verify(mockForm, never()).setError(any(String.class));
            Mockito.verify(mockContext, never()).failureChallenge(any(), any());
            Mockito.verify(mockContext).success();
        }
    }

}
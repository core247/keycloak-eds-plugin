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

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import kz.core247.keycloak.eds.kalkan.CertificateMetadata;
import kz.core247.keycloak.eds.kalkan.CertificateParser;
import kz.core247.keycloak.eds.kalkan.LibCertificateException;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.browser.AbstractUsernameFormAuthenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The EdsLoginAuthenticator class is an abstract class that provides a base implementation for authenticating users using EDS (Electronic Digital Signature) certificates.
 * It extends the AbstractUsernameFormAuthenticator class and implements the Authenticator interface.
 *
 * <p>Subclasses of EdsLoginAuthenticator must implement the validate method to define the specific verification logic for the user and certificate.
 */
public abstract class EdsLoginAuthenticator extends AbstractUsernameFormAuthenticator implements Authenticator {

    private static final Logger logger = LoggerFactory.getLogger(EdsLoginAuthenticator.class);
    private static final String FTL_CHOOSE_EDS = "choose-eds.ftl";

    public EdsLoginAuthenticator() {
    }

    /**
     * Process the authentication action for the EdsLoginAuthenticator.
     *
     * @param context The AuthenticationFlowContext object containing the current authentication context
     */
    @Override
    public void action(AuthenticationFlowContext context) {
        logger.debug("Trying to process form action.");
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();

        String certificateValue = formData.getFirst("certificate");
        if (null == certificateValue) {
            logger.warn("Will be open choose eds form.");
            context.challenge(context.form().createForm(FTL_CHOOSE_EDS));
        }
        CertificateMetadata certificate = null;
        try {
            certificate = CertificateParser.parseMetadata(certificateValue, isIndividual());
        } catch (LibCertificateException e) {
            logger.error("Error during parsing certificate", e);
            logger.trace("Certificate value is: [{}]", certificateValue);
            Response response = buildResponse(context.form(), e.getMessage(), Response.Status.UNAUTHORIZED);
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, response);
            return;
        }
        UserModel user = getUser(context);
        if (null == user || !user.isEnabled()) {
            logger.error("User is not authorized to continue: {}", (null == user) ? "Cannot identify user" : "User is not enabled");
            Response response = buildResponse(context.form(), "User is not authenticated.", Response.Status.UNAUTHORIZED);
            context.failureChallenge(AuthenticationFlowError.ACCESS_DENIED, response);
            return;
        }
        String username = user.getUsername();

        if (!validate(user, certificate)) {
            logger.warn("{} not matched by any verifier.", username);
            Response response = buildResponse(context.form(), "User not verified", Response.Status.BAD_REQUEST);
            context.failureChallenge(AuthenticationFlowError.ACCESS_DENIED, response);
            return;
        }
        context.setUser(user);
        context.success();
    }

    /**
     * Validates a user against the provided certificate metadata.
     *
     * @param user        The UserModel representing the user to be validated.
     * @param certificate The CertificateMetadata representing the metadata of the certificate.
     * @return true if the user is validated successfully, false otherwise.
     */
    public abstract boolean validate(UserModel user, CertificateMetadata certificate);

    /**
     * Builds a response object based on the provided login form, error message, and status.
     *
     * @param form         The login form provider.
     * @param errorMessage The error message to be displayed on the error page.
     * @param status       The status code of the response.
     * @return The built response object.
     */
    private Response buildResponse(LoginFormsProvider form, String errorMessage, Response.Status status) {
        return form.setError(errorMessage).createErrorPage(status);
    }

    /**
     * Retrieves the authenticated user from the current authentication session.
     *
     * @param context The AuthenticationFlowContext object containing the current authentication context.
     * @return The authenticated user from the session.
     */
    private UserModel getUser(AuthenticationFlowContext context) {
        logger.debug("Trying to identify authenticated user.");
        AuthenticationSessionModel sessionModel = context.getAuthenticationSession();
        return sessionModel.getAuthenticatedUser();
    }

    /**
     * Authenticates the user by presenting the choose eds form.
     *
     * @param context The AuthenticationFlowContext object containing the current authentication context
     */
    @Override
    public void authenticate(AuthenticationFlowContext context) {
        logger.info("Trying to authenticate. Will be open choose eds form.");
        context.challenge(context.form().createForm(FTL_CHOOSE_EDS));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requiresUser() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
    }


    /**
     * Indicates whether the authentication method is for individual users or not.
     *
     * @return true if the method is for individual users, false otherwise.
     */
    public abstract boolean isIndividual();
}

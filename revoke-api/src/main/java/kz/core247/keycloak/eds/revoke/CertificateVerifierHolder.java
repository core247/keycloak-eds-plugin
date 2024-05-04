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
package kz.core247.keycloak.eds.revoke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CertificateVerifierHolder class is responsible for managing and retrieving instances of CommonCertificateVerifier.
 * It provides methods to retrieve the verifier based on a specified flag and to reset the verifiers.
 */
public final class CertificateVerifierHolder {

    private static final Logger logger = LoggerFactory.getLogger(CertificateVerifierHolder.class);

    private CertificateVerifierHolder() {
    }

    private static CommonCertificateVerifier individualVerifier;
    private static CommonCertificateVerifier corporateVerifier;

    /**
     * Retrieves the CommonCertificateVerifier based on the specified flag.
     *
     * @param isIndividual a flag indicating whether the verifier is for individual or corporate certificates
     * @return the CommonCertificateVerifier instance
     */
    public static CommonCertificateVerifier getVerifier(boolean isIndividual) {
        if (isIndividual) {
            if (individualVerifier == null) {
                individualVerifier = loadClass(System.getProperty("INDIVIDUAL_CERT_VERIFIER_CLASS_NAME"));
            }
            return individualVerifier;
        } else {
            if (corporateVerifier == null) {
                corporateVerifier = loadClass(System.getProperty("CORPORATE_CERT_VERIFIER_CLASS_NAME"));
            }
            return corporateVerifier;
        }
    }

    private static CommonCertificateVerifier loadClass(String className) {
        CommonCertificateVerifier verifier = null;
        try {
            if (className == null || className.isEmpty()) {
                throw new IllegalArgumentException("Class name not provided.");
            }
            Class<?> clazz = Class.forName(className);
            verifier = (CommonCertificateVerifier) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            logger.error("Cannot instantiate className {}. Will be used {} instance.", className, NullCommonCertificateVerifier.class.getSimpleName(), e);
            verifier = new NullCommonCertificateVerifier();
        }
        return verifier;
    }

    public static void reset() {
        individualVerifier = null;
        corporateVerifier = null;
    }
}

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests the functionality of {@link CertificateVerifierHolder} class.
 */
public class CertificateVerifierHolderTest {

    @BeforeEach
    public void reset() {
        CertificateVerifierHolder.reset();
        System.setProperty("INDIVIDUAL_CERT_VERIFIER_CLASS_NAME", "");
        System.setProperty("CORPORATE_CERT_VERIFIER_CLASS_NAME", "");
    }


    @Test
    public void testGetVerifierIsInstanceOfNullJuridicalCertificateVerifierOnInvalidEnvironment() throws Exception {
        Field singleton = CertificateVerifierHolder.class.getDeclaredField("individualVerifier");
        singleton.setAccessible(true);
        singleton.set(null, null);
        CommonCertificateVerifier verifier = CertificateVerifierHolder.getVerifier(true);
        assertThat(verifier).isInstanceOf(NullCommonCertificateVerifier.class);
    }

    @Test
    public void testGetVerifierReturnsSameInstance() throws Exception {
        Field singleton = CertificateVerifierHolder.class.getDeclaredField("individualVerifier");
        singleton.setAccessible(true);
        singleton.set(null, null);

        String verifierClassName = CustomIndividualCertificateVerifier.class.getName();
        System.setProperty("INDIVIDUAL_CERT_VERIFIER_CLASS_NAME", verifierClassName);

        CommonCertificateVerifier verifier = CertificateVerifierHolder.getVerifier(true);
        CommonCertificateVerifier anotherVerifier = CertificateVerifierHolder.getVerifier(true);

        assertThat(verifier).isEqualTo(anotherVerifier);
        assertThat(verifier).isInstanceOf(CustomIndividualCertificateVerifier.class);
        assertThat(anotherVerifier).isInstanceOf(CustomIndividualCertificateVerifier.class);
    }
}

class CustomIndividualCertificateVerifier implements CommonCertificateVerifier {

    public CustomIndividualCertificateVerifier() {
    }

    @Override
    public boolean verifyCertificateRevocation(String certificateSerialNumber) {
        return new Random().nextBoolean();
    }
}
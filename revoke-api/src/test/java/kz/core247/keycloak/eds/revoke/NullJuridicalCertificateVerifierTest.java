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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * The NullJuridicalCertificateVerifierTest class is a class for testing
 * the verifyCertificateRevocation method of the NullJuridicalCertificateVerifier class.
 *
 * The verifyCertificateRevocation method of the NullJuridicalCertificateVerifier always
 * returns true, regardless of the string input it receives. This test class verifies
 * that behavior.
 */
public class NullJuridicalCertificateVerifierTest {

    @Test  
    void verifyCertificateRevocation_WithEmptyString_ReturnsTrue() {
        NullCommonCertificateVerifier verifier = new NullCommonCertificateVerifier();
        assertDoesNotThrow(() -> {
            assertTrue(verifier.verifyCertificateRevocation(""));
        });
    }
  
    @Test  
    void verifyCertificateRevocation_WithNull_ReturnsTrue() {
        NullCommonCertificateVerifier verifier = new NullCommonCertificateVerifier();
        assertDoesNotThrow(() -> {
            assertTrue(verifier.verifyCertificateRevocation(null));
        });
    }

    @Test  
    void verifyCertificateRevocation_WithAnyString_ReturnsTrue() {
        NullCommonCertificateVerifier verifier = new NullCommonCertificateVerifier();
        assertDoesNotThrow(() -> {
            assertTrue(verifier.verifyCertificateRevocation("anyString"));
        });
    }
}
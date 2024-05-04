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

/**
 * The NullCommonCertificateVerifier class is an implementation of the CommonCertificateVerifier interface.
 * It is used when no actual certificate verification is needed and the verification always returns true.
 */
public class NullCommonCertificateVerifier implements CommonCertificateVerifier {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyCertificateRevocation(String certificate) throws CertificateVerifierException {
        return true;
    }
}

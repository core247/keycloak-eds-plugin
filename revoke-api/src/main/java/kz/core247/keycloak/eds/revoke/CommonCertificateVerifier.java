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
 * The CommonCertificateVerifier interface represents a contract for classes that are responsible for verifying the revocation status of a certificate.
 */
public interface CommonCertificateVerifier {

    /**
     * Verifies the revocation status of a certificate.
     *
     * @param certificateSerialNumber the serial number of the certificate
     * @return true if the certificate is not revoked, false otherwise
     * @throws CertificateVerifierException if an error occurs during the verification process
     */
    boolean verifyCertificateRevocation(String certificateSerialNumber) throws CertificateVerifierException;
}

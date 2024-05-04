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
package kz.core247.keycloak.eds.kalkan;

/**
 * The LibCertificateException class is a custom exception that can be thrown when there is an error related to certificate handling in a library.
 * It extends the Exception class.
 */
public class LibCertificateException extends Exception {

    public LibCertificateException(String message) {
        super(message);
    }

    public LibCertificateException(String message, Throwable cause) {
        super(message, cause);
    }

}

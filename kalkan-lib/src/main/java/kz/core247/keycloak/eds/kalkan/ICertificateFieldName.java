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
 * The ICertificateFieldName interface defines constants for common fields in a certificate.
 * These constants represent the field names that can be used to extract values from a certificate.
 */
public interface ICertificateFieldName {

    String DN = "dn";
    String COMMON_NAME = "commonName";
    String NAME = "name";
    String SURNAME = "surname";
    String LASTNAME = "lastName";
    String BIN = "bin";
    String IIN = "iin";
    String COUNTRY = "country";
    String LOCALITY = "locality";
    String STATE = "state";
    String EMAIL = "email";
    String ORGANIZATION = "organization";
    String BIRTHDATE = "birthDate";
    String GENDER = "gender";
    String NOT_BEFORE = "notBefore";
    String NOT_AFTER = "notAfter";
}

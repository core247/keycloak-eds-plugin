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

/**
 * The KeycloakAttributes interface defines constants for the attribute names used in Keycloak.
 * These constants represent the attributes related to a user's tax code and user class.
 */
public interface KeycloakAttributes {

    String TAX_CODE = "initials";
    String BIN = "userClass";
}

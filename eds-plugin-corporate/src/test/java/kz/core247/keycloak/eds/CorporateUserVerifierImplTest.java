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

import kz.core247.keycloak.eds.kalkan.CertificateMetadata;
import org.junit.Test;
import org.keycloak.models.UserModel;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CorporateUserVerifierImplTest {

    @Test
    public void testVerifyValidData() {
        CertificateMetadata metadata = new CertificateMetadata();
        metadata.withBin("BIN_VALUE");
        metadata.withTaxCode("TAX_CODE_VALUE");

        UserModel user = Mockito.mock(UserModel.class);
        HashMap<String, List<String>> attributes = new HashMap<>();
        List<String> taxCodeList = new ArrayList<>();
        taxCodeList.add("TAX_CODE_VALUE");
        attributes.put(KeycloakAttributes.TAX_CODE, taxCodeList);
        List<String> binList = new ArrayList<>();
        binList.add("BIN_VALUE");
        attributes.put(KeycloakAttributes.BIN, binList);
        Mockito.when(user.getAttributes()).thenReturn(attributes);

        var res = CorporateUserVerifierImpl.verify(user, metadata);

        assertThat(res).isTrue();
    }

    @Test
    public void testVerifyInvalidData() {
        CertificateMetadata metadata = new CertificateMetadata();
        metadata.withBin("ANOTHER_BIN_VALUE");
        metadata.withTaxCode("ANOTHER_TAX_CODE_VALUE");

        UserModel user = Mockito.mock(UserModel.class);
        HashMap<String, List<String>> attributes = new HashMap<>();
        List<String> taxCodeList = new ArrayList<>();
        taxCodeList.add("TAX_CODE_VALUE");
        attributes.put(KeycloakAttributes.TAX_CODE, taxCodeList);
        List<String> binList = new ArrayList<>();
        binList.add("BIN_VALUE");
        attributes.put(KeycloakAttributes.BIN, binList);
        Mockito.when(user.getAttributes()).thenReturn(attributes);

        var res = CorporateUserVerifierImpl.verify(user, metadata);

        assertThat(res).isFalse();
    }

    @Test
    public void testVerifyEmptyData() {
        CertificateMetadata metadata = new CertificateMetadata();
        metadata.withBin("BIN_VALUE");
        metadata.withTaxCode("TAX_CODE_VALUE");

        UserModel user = Mockito.mock(UserModel.class);
        HashMap<String, List<String>> attributes = new HashMap<>();
        Mockito.when(user.getAttributes()).thenReturn(attributes);

        var res = CorporateUserVerifierImpl.verify(user, metadata);

        assertThat(res).isFalse();
    }

}
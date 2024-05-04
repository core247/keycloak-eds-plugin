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

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EdsUtilsTest {

    @Test
    void parseDnTest() {
        // Mock the certificate
        X509Certificate mockedCert = mock(X509Certificate.class);
        when(mockedCert.getNotBefore()).thenReturn(Date.from(LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(mockedCert.getNotAfter()).thenReturn(Date.from(LocalDate.of(2024, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        
        // Test parsing of DN
        String dn = "CN=John Doe,SURNAME=Doe,C=US,O=Test,L=Test City,S=Test State,OU=1234,G=Doe,SERIALNUMBER=BIN5678";
        JSONObject result = EdsUtils.parseDn(dn, mockedCert);

        assertEquals("John Doe", result.get(ICertificateFieldName.COMMON_NAME));
        assertEquals("Doe", result.get(ICertificateFieldName.SURNAME));
        assertEquals("US", result.get(ICertificateFieldName.COUNTRY));
        assertEquals("Test", result.get(ICertificateFieldName.ORGANIZATION));
        assertEquals("Test City", result.get(ICertificateFieldName.LOCALITY));
        assertEquals("Test State", result.get(ICertificateFieldName.STATE));
        assertEquals("1234", result.get(ICertificateFieldName.BIN));
        assertEquals("Doe", result.get(ICertificateFieldName.LASTNAME));
        assertTrue(result.containsKey(ICertificateFieldName.NOT_BEFORE));
        assertTrue(result.containsKey(ICertificateFieldName.NOT_AFTER));
    }
}
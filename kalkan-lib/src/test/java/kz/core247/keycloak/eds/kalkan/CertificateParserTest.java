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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@Disabled
public class CertificateParserTest {

    @Test
    public void testParseMetadataIndividual() throws LibCertificateException {
        try (MockedStatic<DateUtils> dateUtilsMock = Mockito.mockStatic(DateUtils.class)) {
            String certificateValue = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><login><timeTicket>1714813040235</timeTicket><sessionid>1e118c1b-a8a8-47d0-8c16-d1a61fcc282e</sessionid><ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                    "<ds:SignedInfo>\n" +
                    "<ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>\n" +
                    "<ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/>\n" +
                    "<ds:Reference URI=\"\">\n" +
                    "<ds:Transforms>\n" +
                    "<ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\n" +
                    "<ds:Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments\"/>\n" +
                    "</ds:Transforms>\n" +
                    "<ds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/>\n" +
                    "<ds:DigestValue>XZi1+7/71b1kG/TKcBIIwM57VYuOMXA+lDqAXxrW68s=</ds:DigestValue>\n" +
                    "</ds:Reference>\n" +
                    "</ds:SignedInfo>\n" +
                    "<ds:SignatureValue>\n" +
                    "jA7Oxr0cMA6jEyErXXUc42iS7q8e9fQVUBLb7ved2YkaSCxnS/W2ucXFcaHz4l93Udhww3vJ9XDy\n" +
                    "KcLuAMeBW9CS8WCKjakFZtaWjaSmJl5lU/OEo9yEAyfN9ZyCkakpMQ7O1cMLQqcng1dZyFuSGx/I\n" +
                    "j/SPDg8q3hj99Ry/1qoeFhJRnykfYV6hQZGkwOSHpj3QnXKvFUq760U2NMP8ZtrGvwyrD3w6akZ9\n" +
                    "DSqcURcEZGUasdDKgycBzOunBPrm3HYH4PFiRirIoFXspKUeAg2b4XLwycgsNOxs1O9o0Roh65td\n" +
                    "yfAaikjcxrkLuVmpp0n76fV6TvdmWGehYGX9lA==\n" +
                    "</ds:SignatureValue>\n" +
                    "<ds:KeyInfo>\n" +
                    "<ds:X509Data>\n" +
                    "<ds:X509Certificate>\n" +
                    "MIIGEzCCA/ugAwIBAgIUHRc/YSRcGg/FfNz+aWAyqR49zw0wDQYJKoZIhvcNAQELBQAwLTELMAkG\n" +
                    "A1UEBhMCS1oxHjAcBgNVBAMMFdKw0JrQniAzLjAgKFJTQSBURVNUKTAeFw0yMjExMTcxMTI0MjFa\n" +
                    "Fw0yMzExMTcxMTI0MjFaMHkxHjAcBgNVBAMMFdCi0JXQodCi0J7QkiDQotCV0KHQojEVMBMGA1UE\n" +
                    "BAwM0KLQldCh0KLQntCSMRgwFgYDVQQFEw9JSU4xMjM0NTY3ODkwMTExCzAJBgNVBAYTAktaMRkw\n" +
                    "FwYDVQQqDBDQotCV0KHQotCe0JLQmNCnMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA\n" +
                    "mC6NLbWcRlVSq+mdTvUVhRnC1bTSmgc6YQ9pMCkM4/jTSDL3OqFtYu0QmbyR8/gnWgTl9lRL2xyx\n" +
                    "renYn7slSPXIzMOzqiDzG4bTD43kTl04NtRDs3mZH320vB4m0uvR809bog+QOCYswsqKq/oAdnWs\n" +
                    "bbUsnlKt3WfwEiAZgptqLZIw/B/3jxx3to2XxtHh+PpPNRvvaS0HmUwuBNLIkIOX5McmMG4FgprD\n" +
                    "QGluHuQV3WE3do0kbWylUtmBg1yk+qGqzlEiQCKtpfMRyfG+foOyODwrHxfkQZHVZf25qaC4dpSL\n" +
                    "V4pRP0p9SxV9jx/KKlI59oYgLePzfC8J0rFMhwIDAQABo4IB3TCCAdkwPAYDVR0fBDUwMzAxoC+g\n" +
                    "LYYraHR0cDovL3Rlc3QucGtpLmdvdi5rei9jcmwvbmNhX3JzYV90ZXN0LmNybDA+BgNVHS4ENzA1\n" +
                    "MDOgMaAvhi1odHRwOi8vdGVzdC5wa2kuZ292Lmt6L2NybC9uY2FfZF9yc2FfdGVzdC5jcmwwcgYI\n" +
                    "KwYBBQUHAQEEZjBkMDgGCCsGAQUFBzAChixodHRwOi8vdGVzdC5wa2kuZ292Lmt6L2NlcnQvbmNh\n" +
                    "X3JzYV90ZXN0LmNlcjAoBggrBgEFBQcwAYYcaHR0cDovL3Rlc3QucGtpLmdvdi5rei9vY3NwLzAO\n" +
                    "BgNVHQ8BAf8EBAMCBaAwHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCqDDgMDBAEBMF4GA1UdIARXMFUw\n" +
                    "UwYHKoMOAwMCBDBIMCEGCCsGAQUFBwIBFhVodHRwOi8vcGtpLmdvdi5rei9jcHMwIwYIKwYBBQUH\n" +
                    "AgIwFwwVaHR0cDovL3BraS5nb3Yua3ovY3BzMB0GA1UdDgQWBBQNFz9hJFwaD8V83P5pYDKpHj3P\n" +
                    "DTAfBgNVHSMEGDAWgBSmjBYzfLjoNWcGPl5BV1WirzRQaDAWBgYqgw4DAwUEDDAKBggqgw4DAwUB\n" +
                    "ATANBgkqhkiG9w0BAQsFAAOCAgEA04XM2heTWDrAwiJMysiKxUd6JTnBGdEK1wXi8H3XRfTXW3Tk\n" +
                    "Kj9phbpdU3NJ1goic8rd5nsrPqcWFRD38ptdYRgzRYB2VPPFfEhNlwUmFpeif8qWbN25HClFIrzx\n" +
                    "uDn4Y0AslqD/XeTnpUg4tCM0tYgRttir9CYXg3ofj2YsGlfp4zGgT3SZHLhfds/l+UYgNb8B5Jjp\n" +
                    "akiErwDWoX5JivCN48rQt3hjg9uXruveNQOBSlvBUuffeyDRYIV/lsRVpA7Gjju+oapLsHtlbpSa\n" +
                    "Mm72SinBXzS42NpcPviAUjEq1YYySufA25jhWxFuwm+CMHV92sF+rsFFamJpyghfcNZVCSx7t8as\n" +
                    "p7KqEtpPq6NfzPyjMvi14MVzuHnUrhOO9oRwdDGqQcdvPeM+lMJd4JU7FItEUrD0I0FYuJAuUATt\n" +
                    "iLlYOYbl7aDVGvFSoHEkn0RYt1qlzR88X5bwuvMiQgZSFo0H2PT5Wyjw82gF1ffRWRW+/S2mI1Ov\n" +
                    "q9Zu8xD7+/CR/zwi6GUPfu+hEJqTKAVsmgQzJq/Tv1OLMrazi27v/W4fdAWQKFpFJ0jld/ErR9pM\n" +
                    "NgGBEUNDiZL00sLqUh1Fk/l/Ov1defG7Y9xWmVS9Uat2zf1RZCnEH2V86gJdXMCrDaPJwVvl03W1\n" +
                    "d9myXSZaHO+L97guwBJ1pGJJU3U=\n" +
                    "</ds:X509Certificate>\n" +
                    "</ds:X509Data>\n" +
                    "</ds:KeyInfo>\n" +
                    "</ds:Signature></login>\n";

            Date expireDateDayBefore = Date.from(LocalDate.of(2023, 11, 16).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            dateUtilsMock.when(() -> DateUtils.getCurrentDate()).thenReturn(expireDateDayBefore);
            CertificateMetadata metadata = CertificateParser.parseMetadata(certificateValue, true);
            assertNotNull(metadata, "Failed to parse certificate metadata.");

            // Validate individual fields of the parsed certificate (replace dummy values with actual expected ones)
            assertEquals("123456789011", metadata.getTaxCode(), "Tax code not matched.");
            assertEquals("ТЕСТ", metadata.getName(), "Name not matched.");
            assertEquals("ТЕСТОВ", metadata.getSurname(), "Surname not matched.");
            assertEquals("ТЕСТОВИЧ", metadata.getPatronymic(), "Patronymic not matched.");
            assertEquals("", metadata.getEmail(), "Email not matched.");
            assertEquals("", metadata.getBirtDate(), "Birth date not matched.");
            assertEquals("KZ", metadata.getCountry(), "Country not matched.");
            assertEquals("", metadata.getGender(), "Gender not matched.");
            assertEquals("", metadata.getNotBefore(), "Not before date not matched.");
            assertEquals("", metadata.getNotAfter(), "Not after date not matched.");
            assertEquals("CN=ТЕСТОВ ТЕСТ,SURNAME=ТЕСТОВ,SERIALNUMBER=IIN123456789011,C=KZ,G=ТЕСТОВИЧ", metadata.getDn(), "DN not matched.");
            assertEquals("", metadata.getOrganization(), "Organization not matched.");
            assertEquals("", metadata.getBin(), "BIN not matched.");
        }
    }

    @Test
    public void testParseMetadataCorporate() throws LibCertificateException {
        try (MockedStatic<DateUtils> dateUtilsMock = Mockito.mockStatic(DateUtils.class)) {
            String certificateValue = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><login><timeTicket>1714814361503</timeTicket><sessionid>86ed9a1d-c753-444b-a5a0-d0370ef32a5a</sessionid><ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                    "<ds:SignedInfo>\n" +
                    "<ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>\n" +
                    "<ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/>\n" +
                    "<ds:Reference URI=\"\">\n" +
                    "<ds:Transforms>\n" +
                    "<ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\n" +
                    "<ds:Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments\"/>\n" +
                    "</ds:Transforms>\n" +
                    "<ds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/>\n" +
                    "<ds:DigestValue>/Bg+fR7MjDLjd6cSq+PC/d5tKBlN5OJwXwBgv3FOCQU=</ds:DigestValue>\n" +
                    "</ds:Reference>\n" +
                    "</ds:SignedInfo>\n" +
                    "<ds:SignatureValue>\n" +
                    "fJnVStCdI3iYWxy4mQaVt+xcy67FUjRl4V9N1wlge6q67CR11Q1UaCpvaAN/Fgb8GvTmeGJXCScJ\n" +
                    "s/t8suCep08AGwxDNOJbBuZzBPGS/Cm3gBG8j8QiXFzzsBj+sxGHUA1kZAXG4KTJ492+X+5fsfKZ\n" +
                    "rjZCm9TJim7NBj+ts08v0GvZOywbs5/N49EKG8QgnKv2gOh8bnMc1Mlfe9e9r479FHiEcdoJVs3S\n" +
                    "RwzemzFqxp6lSkXT7UDgBamDvarYHJAmFvBG9lpSEE5fLYHePVFM4WPyV7MjUYVTqsKRuAmzp9f0\n" +
                    "eeyCk2RzvrDBLzlFb/kXAW4vMdW8PCL5WZ40hg==\n" +
                    "</ds:SignatureValue>\n" +
                    "<ds:KeyInfo>\n" +
                    "<ds:X509Data>\n" +
                    "<ds:X509Certificate>\n" +
                    "MIIGVDCCBDygAwIBAgIUfxaIMGG09/JaVUyUY1vNFc0gg38wDQYJKoZIhvcNAQELBQAwLTELMAkG\n" +
                    "A1UEBhMCS1oxHjAcBgNVBAMMFdKw0JrQniAzLjAgKFJTQSBURVNUKTAeFw0yMzAyMDExMDQ0Mjha\n" +
                    "Fw0yNDAyMDExMDQ0MjhaMIGtMR4wHAYDVQQDDBXQotCV0KHQotCe0JIg0KLQldCh0KIxFTATBgNV\n" +
                    "BAQMDNCi0JXQodCi0J7QkjEYMBYGA1UEBRMPSUlOMTIzNDU2Nzg5MDExMQswCQYDVQQGEwJLWjEY\n" +
                    "MBYGA1UECgwP0JDQniAi0KLQldCh0KIiMRgwFgYDVQQLDA9CSU4xMjM0NTY3ODkwMjExGTAXBgNV\n" +
                    "BCoMENCi0JXQodCi0J7QktCY0KcwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCsrfZW\n" +
                    "m/1c6mvwrn8xxeU6GqKLUfVcxO5p330lSPT3iCkQgXK93DAPnB2FEhQHWIOo8w7exGpvxzXUNQfL\n" +
                    "oWod/8o0Gcx+WhogZ8jSUwnkftDUD/LkgJkw6fp5r/ZNFTbZmpEakIWJiHmRgN26STvZAHFRsad6\n" +
                    "LfrzlpKjlE+v8Lw1gCZRbQ+yh2hG+1l33AzZDM1tV+rzeX9AL5gzelt6oLWLvZgSofol8CoPYy02\n" +
                    "9eEmY6vCqd1SJ3608Hqa0jwxUNd5oCPWEUwtphncawI5Y5Tvy1NTOCTOd/N4BJpALV0w1HFU7sec\n" +
                    "daFbSl6Ho/c7ZKVYlHalUdxaENi0/nlFAgMBAAGjggHpMIIB5TAOBgNVHQ8BAf8EBAMCBaAwKAYD\n" +
                    "VR0lBCEwHwYIKwYBBQUHAwIGCCqDDgMDBAECBgkqgw4DAwQBAgEwXgYDVR0gBFcwVTBTBgcqgw4D\n" +
                    "AwICMEgwIQYIKwYBBQUHAgEWFWh0dHA6Ly9wa2kuZ292Lmt6L2NwczAjBggrBgEFBQcCAjAXDBVo\n" +
                    "dHRwOi8vcGtpLmdvdi5rei9jcHMwPAYDVR0fBDUwMzAxoC+gLYYraHR0cDovL3Rlc3QucGtpLmdv\n" +
                    "di5rei9jcmwvbmNhX3JzYV90ZXN0LmNybDA+BgNVHS4ENzA1MDOgMaAvhi1odHRwOi8vdGVzdC5w\n" +
                    "a2kuZ292Lmt6L2NybC9uY2FfZF9yc2FfdGVzdC5jcmwwcwYIKwYBBQUHAQEEZzBlMDkGCCsGAQUF\n" +
                    "BzAChi1odHRwOi8vdGVzdC5wa2kuZ292Lmt6L2NlcnQvbmNhX2dvc3RfdGVzdC5jZXIwKAYIKwYB\n" +
                    "BQUHMAGGHGh0dHA6Ly90ZXN0LnBraS5nb3Yua3ovb2NzcC8wHQYDVR0OBBYEFH8WiDBhtPfyWlVM\n" +
                    "lGNbzRXNIIN/MB8GA1UdIwQYMBaAFKaMFjN8uOg1ZwY+XkFXVaKvNFBoMBYGBiqDDgMDBQQMMAoG\n" +
                    "CCqDDgMDBQEBMA0GCSqGSIb3DQEBCwUAA4ICAQCSfrHvobShE6ZZVuzf3QL2E6j9+m5vUOVk/eCP\n" +
                    "wkkCPcEANRT9gUHxM1ZAv01lR2lkOwDOPUWoBlmXxtBhqY7Aeg48HA1vfDR4YH1fRLu9EX9n2ctx\n" +
                    "vWN8IOU8ZtELCKumwkwESy8c5URC+N9xtpzdMIm95Q9+hDjG6Fs533/EtdUsq1kE0RjKYQ7AFa31\n" +
                    "tPzrVgW94QPqFhKvq3okDeAoX+mmE4cN+HC0fqWubS+5tedTDRNQuzQmFJOueo1nmWFE1oJWAg4P\n" +
                    "PljM+Kbp8pZPq59IDfkcYuWmUJ+B6VDgN2N09xrZ+zUyvhdm/RJ5ky5qKVD6kStpq7SCceBrC1pE\n" +
                    "fgZRrtCSuTRweWWf+i0eJhFnhcUDORBugDkitymPU5Oz3FtGG7+dv6l1zd5Doh7TNNaZe6aWFTKq\n" +
                    "56jUIMamanOJMM2SKTiF8aWcACAlKc3TOKfw0Sx9dO4Df/xnSudxhDMq62f4uxL4juQ0jFwufs+z\n" +
                    "O9KkRf2r6UHPtKfQJCEzLDUdt3zw8XX3P2Yy26sjYDGVnv/Eor0x5hjeH9iyL/JamfqHpYXjXR1R\n" +
                    "VRRzvMNUH2s/3PZsTc/UaHOJYPbr+WHIa/ywKmFq8Yfe07ElvyjrjdAYBn4/24vszTdEn2qAxBHD\n" +
                    "/HWTyQm1aXbVaPbd7ZF/kVt04gDSVE2wz2G/Tg==\n" +
                    "</ds:X509Certificate>\n" +
                    "</ds:X509Data>\n" +
                    "</ds:KeyInfo>\n" +
                    "</ds:Signature></login>";

            Date expireDateDayBefore = Date.from(LocalDate.of(2023, 11, 16).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            dateUtilsMock.when(() -> DateUtils.getCurrentDate()).thenReturn(expireDateDayBefore);
            CertificateMetadata metadata = CertificateParser.parseMetadata(certificateValue, false);
            assertNotNull(metadata, "Failed to parse certificate metadata.");

            assertEquals("123456789011", metadata.getTaxCode(), "Tax code not matched.");
            assertEquals("ТЕСТ", metadata.getName(), "Name not matched.");
            assertEquals("ТЕСТОВ", metadata.getSurname(), "Surname not matched.");
            assertEquals("ТЕСТОВИЧ", metadata.getPatronymic(), "Patronymic not matched.");
            assertEquals("", metadata.getEmail(), "Email not matched.");
            assertEquals("", metadata.getBirtDate(), "Birth date not matched.");
            assertEquals("KZ", metadata.getCountry(), "Country not matched.");
            assertEquals("", metadata.getGender(), "Gender not matched.");
            assertEquals("", metadata.getNotBefore(), "Not before date not matched.");
            assertEquals("", metadata.getNotAfter(), "Not after date not matched.");
            assertEquals("CN=ТЕСТОВ ТЕСТ,SURNAME=ТЕСТОВ,SERIALNUMBER=IIN123456789011,C=KZ,O=АО \\\"ТЕСТ\\\",OU=BIN123456789021,G=ТЕСТОВИЧ", metadata.getDn(), "DN not matched.");
            assertEquals("АО \"ТЕСТ\"", metadata.getOrganization(), "Organization not matched.");
            assertEquals("123456789021", metadata.getBin(), "BIN not matched.");
        }
    }
}
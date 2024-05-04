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

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The EdsUtils class provides utility methods for parsing and processing X509 certificates.
 */
public class EdsUtils {

    private static final Logger logger = LoggerFactory.getLogger(EdsUtils.class);

    private static final Map<String, BiConsumer<Rdn, JSONObject>> rdnTypeConsumerMap = new HashMap<>();

    static {
        rdnTypeConsumerMap.put("CN", (rdn, subject) -> putCommonName(rdn, subject));
        rdnTypeConsumerMap.put("SURNAME", (rdn, subject) -> subject.put(ICertificateFieldName.SURNAME, rdn.getValue()));
        rdnTypeConsumerMap.put("SERIALNUMBER", (rdn, subject) -> putSerialNumber(rdn, subject));
        rdnTypeConsumerMap.put("C", (rdn, subject) -> subject.put(ICertificateFieldName.COUNTRY, rdn.getValue()));
        rdnTypeConsumerMap.put("L", (rdn, subject) -> subject.put(ICertificateFieldName.LOCALITY, rdn.getValue()));
        rdnTypeConsumerMap.put("S", (rdn, subject) -> subject.put(ICertificateFieldName.STATE, rdn.getValue()));
        rdnTypeConsumerMap.put("O", (rdn, subject) -> subject.put(ICertificateFieldName.ORGANIZATION, rdn.getValue()));
        rdnTypeConsumerMap.put("OU", (rdn, subject) -> subject.put(ICertificateFieldName.BIN, ((String) rdn.getValue()).replaceAll("^BIN", "")));
        rdnTypeConsumerMap.put("G", (rdn, subject) -> subject.put(ICertificateFieldName.LASTNAME, rdn.getValue()));
    }

    private EdsUtils() {
    }

    /**
     * Parses the DN (Distinguished Name) of an X509Certificate and returns a JSONObject representation.
     *
     * @param dn   the DN to parse
     * @param cert the X509Certificate to extract additional information from
     * @return a JSONObject representing the parsed DN and additional information from the certificate
     */
    public static JSONObject parseDn(String dn, X509Certificate cert) {
        JSONObject subject = new JSONObject();
        subject.put(ICertificateFieldName.DN, dn);
        LdapName ldapName;

        try {
            ldapName = new LdapName(dn);
            for (Rdn rdn : ldapName.getRdns()) {
                rdnTypeConsumerMap.getOrDefault(rdn.getType().toUpperCase(), (rdnElement, subjectElement) -> {
                }).accept(rdn, subject);
            }
        } catch (InvalidNameException e) {
            logger.warn("Error during parsing rdn. Process will be continued.", e);
        }

        subject.put(ICertificateFieldName.NOT_BEFORE, DateUtils.dateTime(cert.getNotBefore()));
        subject.put(ICertificateFieldName.NOT_AFTER, DateUtils.dateTime(cert.getNotAfter()));

        return subject;
    }

    /**
     * Puts the serial number of an Rdn into a JSONObject representation of a subject.
     * The cleaned serial number is put into the subject JSONObject using the appropriate key, either "bin" or "iin".
     *
     * @param rdn    the Rdn containing the serial number
     * @param subject the JSONObject representation of a subject
     */
    private static void putSerialNumber(Rdn rdn, JSONObject subject) {
        String sn = (String) rdn.getValue();
        String cleanedSn = sn.startsWith("BIN") ? sn.replaceAll("^BIN", "") : sn.replaceAll("^IIN", "");
        String key = sn.startsWith("BIN") ? ICertificateFieldName.BIN : ICertificateFieldName.IIN;
        subject.put(key, cleanedSn);
    }

    /**
     * Puts the common name (CN) of an Rdn into a JSONObject representation of a subject.
     * The common name is put into the subject JSONObject using the appropriate key - ICertificateFieldName.COMMON_NAME.
     * If the subject contains the surname (ICertificateFieldName.SURNAME) field, it also extracts the name (ICertificateFieldName.NAME)
     * by removing the surname from the common name.
     *
     * @param rdn     the Rdn containing the common name
     * @param subject the JSONObject representation of a subject
     */
    private static void putCommonName(Rdn rdn, JSONObject subject) {
        subject.put(ICertificateFieldName.COMMON_NAME, rdn.getValue());

        if (subject.containsKey(ICertificateFieldName.SURNAME)) {
            String commonName = subject.get(ICertificateFieldName.COMMON_NAME).toString();
            String surname = subject.get(ICertificateFieldName.SURNAME).toString();
            String name = StringUtils.substringAfter(commonName, surname).replaceFirst(" ", "");
            subject.put(ICertificateFieldName.NAME, name);
        }
    }

}

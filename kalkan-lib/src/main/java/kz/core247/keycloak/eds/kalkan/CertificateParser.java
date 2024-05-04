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

import kz.core247.keycloak.eds.revoke.CertificateVerifierException;
import kz.core247.keycloak.eds.revoke.CertificateVerifierHolder;
import kz.core247.keycloak.eds.revoke.CommonCertificateVerifier;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A utility class for parsing certificate metadata from a given XML representation of a certificate.
 */
public final class CertificateParser {

    private static final Logger logger = LoggerFactory.getLogger(CertificateParser.class);
    private static final String XML_DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final String XML_LOAD_EXTERNAL_DTD_FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    private static final String CERTIFICATE_KEY_AUTH = "1.3.6.1.5.5.7.3.2";
    private static final String PROTECTED_EMAIL_KEY = "1.3.6.1.5.5.7.3.4";

    private static final String INDIVIDUAL_KEY = "1.2.398.3.3.4.1.1";

    private static final String CORPORATE_KEY = "1.2.398.3.3.4.1.2";

    /**
     * Parses the metadata of a certificate.
     *
     * @param certificateValue the value of the certificate
     * @return the CertificateMetadata object representing the metadata of the certificate
     * @throws LibCertificateException if the certificate parsing fails
     */
    public static CertificateMetadata parseMetadata(String certificateValue, boolean isIndividual) throws LibCertificateException {
        SecurityProviderInitializer.initKalkanProvider();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XML_DISALLOW_DOCTYPE_DECL_FEATURE, true);
            dbf.setFeature(XML_LOAD_EXTERNAL_DTD_FEATURE, false);
            Element rootEl = getRootElement(dbf, certificateValue);
            return extractDataFromSignature(rootEl, isIndividual);
        } catch (LibCertificateException lce) {
            throw lce;
        } catch (Exception e) {
            throw new LibCertificateException("Failed to parse certificate", e);
        }
    }

    /**
     * Retrieves the root element of an XML document.
     *
     * @param dbf the DocumentBuilderFactory object used to create DocumentBuilder
     * @param signedXml the XML document as a String
     * @return the root element of the XML document
     * @throws IOException if an I/O error occurs
     * @throws SAXException if any SAX parsing errors occur
     * @throws ParserConfigurationException if a DocumentBuilder cannot be created
     */
    private static Element getRootElement(DocumentBuilderFactory dbf, String signedXml) throws IOException, SAXException, ParserConfigurationException {
        dbf.setNamespaceAware(true);
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document doc = documentBuilder.parse(new ByteArrayInputStream(signedXml.getBytes(StandardCharsets.UTF_8)));
        return (Element) doc.getFirstChild();
    }

    /**
     * Extracts the certificate metadata from the given XML signature element.
     *
     * @param rootEl the root XML element containing the signature
     * @return the CertificateMetadata object representing the metadata of the certificate
     * @throws XMLSecurityException if an error occurs during XML security processing
     * @throws CertificateParsingException if an error occurs during certificate parsing
     * @throws LibCertificateException if an error occurs during certificate extraction
     */
    private static CertificateMetadata extractDataFromSignature(Element rootEl, boolean isIndividual) throws XMLSecurityException, CertificateParsingException, LibCertificateException {
        logger.debug("Trying to extract certificate metadata...");
        NodeList list = rootEl.getElementsByTagName("ds:Signature");
        return parseCertificateSignatures(list, isIndividual);
    }

    /**
     * Parses the certificate signatures from a NodeList.
     *
     * @param list the NodeList containing the certificate signatures
     * @return the CertificateMetadata object representing the metadata of the certificate
     * @throws XMLSecurityException if an error occurs during XML security processing
     * @throws CertificateParsingException if an error occurs during certificate parsing
     * @throws LibCertificateException if an error occurs during certificate extraction
     */
    private static CertificateMetadata parseCertificateSignatures(NodeList list, boolean isIndividual)
            throws XMLSecurityException, CertificateParsingException, LibCertificateException {
        int length = list.getLength();
        for (int i = 0; i < length; i++) {
            Node sigNode = list.item(length - 1);
            Element sigElement = checkSignatureElement(sigNode);
            CertificateMetadata certMeta = checkCertificateValidityAndSignature(sigElement, isIndividual);
            if (certMeta != null) {
                return certMeta;
            }
        }
        return null;
    }

    /**
     * Checks the signature element in the XML document.
     *
     * @param sigNode the signature node in the XML document
     * @return the signature element
     * @throws LibCertificateException if the signature element is not found
     */
    private static Element checkSignatureElement(Node sigNode) throws LibCertificateException {
        Element sigElement = (Element) sigNode;
        if (sigElement == null) {
            throw new LibCertificateException("Bad signature: Element 'ds:Reference' is not found in XML document");
        }
        return sigElement;
    }

    /**
     * Checks the validity and signature of a certificate.
     *
     * @param sigElement the XML signature element containing the certificate
     * @return the CertificateMetadata object representing the metadata of the certificate
     * @throws XMLSecurityException if an error occurs during XML security processing
     * @throws CertificateParsingException if an error occurs during certificate parsing
     * @throws LibCertificateException if an error occurs during certificate extraction
     */
    private static CertificateMetadata checkCertificateValidityAndSignature(Element sigElement, boolean isIndividual)
            throws XMLSecurityException, CertificateParsingException, LibCertificateException {
        XMLSignature signature = new XMLSignature(sigElement, "");
        KeyInfo ki = signature.getKeyInfo();
        X509Certificate cert = ki.getX509Certificate();
        if (Objects.nonNull(cert)) {
            validateOnCertificateExpiry(cert);
            validateSignature(signature, cert, isIndividual);
            validateClientAndKeyType(cert.getExtendedKeyUsage(), isIndividual);
            JSONObject jsonObject = EdsUtils.parseDn(cert.getSubjectDN().getName(), cert);
            return toMetadata(jsonObject);
        } else {
            throw new RuntimeException("Certificate is null");
        }
    }

    /**
     * Validates the signature of an XML document using a certificate.
     *
     * @param signature the XMLSignature object representing the signature
     * @param cert the X509Certificate object representing the certificate
     * @throws LibCertificateException if there is an error during certificate validation
     * @throws XMLSignatureException if there is an error during signature validation
     */
    private static void validateSignature(XMLSignature signature, X509Certificate cert, boolean isIndividual) throws LibCertificateException, XMLSignatureException {
        if (!signature.checkSignatureValue(cert)) {
            throw new RuntimeException("Certificate document is not signed");
        }
        validateCertificateRevocation(cert, isIndividual);
    }

    /**
     * Validates the revocation status of a certificate.
     *
     * @param cert the X509Certificate to be validated
     * @throws LibCertificateException if the certificate is revoked or an error occurs during validation
     */
    private static void validateCertificateRevocation(X509Certificate cert, boolean isIndividual) throws LibCertificateException {
        String serialNumber = getFormattedSerialNumberFromCert(cert);
        CommonCertificateVerifier revokeVerifier = CertificateVerifierHolder.getVerifier(isIndividual);
        try {
            Boolean isValid = revokeVerifier.verifyCertificateRevocation(serialNumber);
            if (!isValid) {
                throw new LibCertificateException("Certificate is already revoked");
            }
        } catch (CertificateVerifierException e) {
            logger.error("Error during certificate revocation checking");
            throw new LibCertificateException("Error during certificate revocation checking", e);
        }
    }

    /**
     * Retrieves a formatted serial number from a X509Certificate object.
     *
     * @param cert the X509Certificate object from which to retrieve the serial number
     * @return the formatted serial number as a String
     */
    private static String getFormattedSerialNumberFromCert(X509Certificate cert) {
        byte[] bytesSN = cert.getSerialNumber().toByteArray();
        BigInteger rawSerialNumber = new BigInteger(1, bytesSN);
        String serialNumber = Hex.encodeHexString(rawSerialNumber.toByteArray());
        return StringUtils.deleteWhitespace(serialNumber).toUpperCase();
    }

    /**
     * Validates if the given X509Certificate is not expired or not yet valid.
     * Throws a {@link LibCertificateException} if the certificate is expired or not yet valid.
     *
     * @param cert the X509Certificate to be validated
     * @throws LibCertificateException if the certificate is expired or not yet valid
     */
    private static void validateOnCertificateExpiry(X509Certificate cert) throws LibCertificateException {
        Date currentDate = DateUtils.getCurrentDate();
        if (currentDate.after(cert.getNotAfter())) {
            throw new LibCertificateException("Certificate is already expired.");
        }
        try {
            cert.checkValidity(currentDate);
        } catch (CertificateExpiredException | CertificateNotYetValidException ex) {
            throw new LibCertificateException("Certificate is already not valid.", ex);
        }
    }

    /**
     * Validates the client and key type of a certificate.
     *
     * @param keys the list of extended key usages of the certificate
     * @throws LibCertificateException if the client or key type is invalid
     */
    private static void validateClientAndKeyType(List<String> keys, boolean isIndividual) throws LibCertificateException {
        if (!keys.contains(CERTIFICATE_KEY_AUTH) && !keys.contains(PROTECTED_EMAIL_KEY)) {
            logger.error("Keys {} not contains {} or {}", keys, CERTIFICATE_KEY_AUTH, PROTECTED_EMAIL_KEY);
            throw new LibCertificateException("Certificate type is invalid.");
        }
        if (!isIndividual && !keys.contains(CORPORATE_KEY)) {
            throw new LibCertificateException("Corporate key is not found. Illegal certificate.");
        }
        if (isIndividual && !keys.contains(INDIVIDUAL_KEY)) {
            throw new LibCertificateException("Individual key is not found. Illegal certificate.");
        }
    }

    /**
     * Converts a JSONObject representing a certificate metadata into a CertificateMetadata object.
     *
     * @param jsonObject the JSONObject containing the certificate metadata
     * @return the CertificateMetadata object representing the metadata of the certificate
     */
    private static CertificateMetadata toMetadata(JSONObject jsonObject) {
        String taxCode = getValueOrEmpty(jsonObject, ICertificateFieldName.IIN);
        String name = getValueOrEmpty(jsonObject, ICertificateFieldName.NAME);
        String surname = getValueOrEmpty(jsonObject, ICertificateFieldName.SURNAME);
        String patronymic = getValueOrEmpty(jsonObject, ICertificateFieldName.LASTNAME);
        String email = getValueOrEmpty(jsonObject, ICertificateFieldName.EMAIL);
        String birthDate = getValueOrEmpty(jsonObject, ICertificateFieldName.BIRTHDATE);
        String country = getValueOrEmpty(jsonObject, ICertificateFieldName.COUNTRY);
        String gender = getValueOrEmpty(jsonObject, ICertificateFieldName.GENDER);
        String notBefore = getValueOrEmpty(jsonObject, ICertificateFieldName.NOT_BEFORE);
        String notAfter = getValueOrEmpty(jsonObject, ICertificateFieldName.NOT_AFTER);
        String dn = getValueOrEmpty(jsonObject, ICertificateFieldName.DN);
        String organization = getValueOrEmpty(jsonObject, ICertificateFieldName.ORGANIZATION);
        String bin = getValueOrEmpty(jsonObject, ICertificateFieldName.BIN);

        return new CertificateMetadata()
                .withTaxCode(taxCode)
                .withName(name)
                .withSurname(surname)
                .withPatronymic(patronymic)
                .withEmail(email)
                .withBirtDate(birthDate)
                .withCountry(country)
                .withGender(gender)
                .withNotBefore(notBefore)
                .withNotAfter(notAfter)
                .withDn(dn)
                .withOrganization(organization)
                .withBin(bin);
    }

    private static String getValueOrEmpty(JSONObject jsonObject, String key) {
        Object value = jsonObject.getOrDefault(key, "");
        if (value == null) {
            return "";
        }
        return value.toString();
    }
}

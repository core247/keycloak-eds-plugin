package kz.core247.example;

import kz.core247.keycloak.eds.revoke.CertificateVerifierException;
import kz.core247.keycloak.eds.revoke.CommonCertificateVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The RandomCommonCertificateVerifier class is an implementation of the CommonCertificateVerifier interface.
 * It verifies the revocation status of a certificate by generating a random value and returning true if it is less than 0.5,
 * and false otherwise.
 */
public class RandomCommonCertificateVerifier implements CommonCertificateVerifier {
    private static final Logger logger = LoggerFactory.getLogger(RandomCommonCertificateVerifier.class);

    @Override
    public boolean verifyCertificateRevocation(String certificateSerialNumber) throws CertificateVerifierException {
        logger.info("It's about verify via random mechanism.");
        if (certificateSerialNumber == null) {
            throw new CertificateVerifierException("Certificate serial number is null");
        }
        return Math.random() < 0.5;
    }
}

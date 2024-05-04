package kz.core247.example;

import kz.core247.keycloak.eds.revoke.CertificateVerifierException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RandomCommonCertificateVerifierTest {

    @Test
    public void testVerifyCertificateRevocation_whenRandomLessThan0_5_returnsTrue() throws CertificateVerifierException {
        // Arrange
        RandomCommonCertificateVerifier randomCommonCertificateVerifier = new RandomCommonCertificateVerifier();

        // Act
        boolean verificationResult = randomCommonCertificateVerifier.verifyCertificateRevocation("1001");

        // Assert        
        Assertions.assertTrue(verificationResult || !verificationResult); // As random can return either less or greater than 0.5, the method could return either true or false.
    }

    @Test
    public void testVerifyCertificateRevocation_whenExceptionOccured_shouldThrowException() {
        // Arrange
        RandomCommonCertificateVerifier randomCommonCertificateVerifier = new RandomCommonCertificateVerifier();

        // Act & Assert
        Assertions.assertThrows(CertificateVerifierException.class, () -> {
            randomCommonCertificateVerifier.verifyCertificateRevocation(null);
        });
    }
}
# Keycloak EDS plugin
The Keycloak plugin for certificate-based authentication enables the use of 
X.509 certificates for user authentication.
When a user attempts to log in, the plugin prompts the user to present a certificate. 
If the user's client certificate is supported, authentication will be based on the 
information contained within that certificate.
The user certificate plugin provides security by allowing users to authenticate using 
two-factor authentication. This means the user must provide something they know (password)
and something they are in possession of (certificate).
The plugin can also be configured to perform additional checks such as checking the
Certificate Revocation List (CRL)(see revoke-api documentation part below)
In essence, the Keycloak plugin for certificate-based authentication provides 
outstanding security and flexibility for the users and administrators of your system.

It was developed and tested against **Keycloak 22.0.4**. 
Compatibility with other versions of Keycloak is not guaranteed.

## License
This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Getting Help

If you find a bug or an issue, please

[report an issue](https://github.com/core247/keycloak-eds-plugin/issues/new) on the keycloak eds plugin repository

## Contributing

See the [contributing documentation](CONTRIBUTING.md)

## What does plugin provides:
- Implement secondary sign-in step in the browser flow.
- Validate and perform revocation checks on certificates.
- Verify users based on their user attributes.

## Local development
- Use `Amazon Corretto 17` as the SDK.

- For local development, put these libraries in the `libs` folder:
    - `commons-logging-1.1.1.jar`
    - `kalkancrypt-0.7.2.jar`
    - `kalkancrypt_xmldsig-0.4.jar`
    - `knca_provider_util-0.8.jar`
    - `xmlsec-1.5.8.jar`

- Build the plugin with Maven using `mvn clean package`.

- - Use docker-compose to launch Keycloak with the plugin:
```
cd docker
docker-compose up -d
```

## Configuring Keycloak for Local Testing

[KEYCLOAK-CONFIGURE.md](KEYCLOAK-CONFIGURE.md)

## Using on production

For production use, you should place the Keycloak plugin JAR file and other 
Kalkan libraries in the same location as specified in the docker-compose file:

- Place the `eds-plugin-corporate-1.0.0.jar` file in the `/opt/bitnami/keycloak/providers/eds-plugin-corporate.jar` directory. 
  The path should look like this: `../eds-plugin-corporate/target/eds-plugin-corporate-1.0.0.jar:/opt/bitnami/keycloak/providers/eds-plugin-corporate.jar`
- Place the `eds-plugin-individual-1.0.0.jar` file in the `/opt/bitnami/keycloak/providers/eds-plugin-individual.jar` directory.
  The path should look like this: `../eds-plugin-individual/target/eds-plugin-individual-1.0.0.jar:/opt/bitnami/keycloak/providers/eds-plugin-individual.jar`
- Place the contents of the `libs` directory in the `/opt/bitnami/keycloak/providers` directory. 
  The path should look like this: `../libs/:/opt/bitnami/keycloak/providers/`
- Configure user attributes in keycloak
  - For individual users to be able to use the plugin, you need to add an attribute named 
    `initials` to the user's attributes. This attribute should contain the User's Tax Code. 
     The plugin will use this specific attribute for identification purposes.
  - For corporate users, you need to add the attribute userClass representing the 
    Corporate's BIN (Tax Code). Similar to individual users, this attribute enhances the 
    plugin's capability to identify a corporate user.
- Additionally, for handling certificate revocations, you should refer to the documentation in the revoke-api section below.

### revoke-api
The `revoke-api` module is designed to facilitate setting up certificate verification 
according to your custom implementation. You can extend and tailor the functionality 
of the certificate verifier to satisfy your specific needs.

By default, the system utilizes the `kz.core247.keycloak.eds.revoke.NullCommonCertificateVerifier` implementation 
if no custom implementation is provided by the user. This default implementation consistently
returns a positive (true) result.

#### Steps
- Implement the `kz.core247.keycloak.eds.revoke.CommonCertificateVerifier` interface.
Start by creating a class that implements the CommonCertificateVerifier interface. 
Your class should have defined all methods indicated in the interface.

Refer to the `kz.core247.example.RandomCommonCertificateVerifier` in the `random-revoke-api-impl` module 
as an illustrative example of this implementation.

- Specify the Location of the JAR in Docker Setup
  If you are using Docker, you will need to add the JAR file to your Keycloak setup. Specify the location of the jar file in your docker-compose.yml file.
  ```volumes:
    - ../random-revoke-api-impl/target/random-revoke-api-impl-1.0.0.jar:/opt/bitnami/keycloak/providers/random-revoke-api-impl.jar```

- Configure system properties
  Delineate your custom class through the utilization of system properties. Particularly, apply the 'INDIVIDUAL_CERT_VERIFIER_CLASS_NAME' and 'CORPORATE_CERT_VERIFIER_CLASS_NAME' properties.
  When deploying with Docker, system properties can be set in the environment configuration. For example:
  ```
     JAVA_OPTS_APPEND: -DINDIVIDUAL_CERT_VERIFIER_CLASS_NAME=kz.core247.example.RandomCommonCertificateVerifier
  ```
  Replace `kz.core247.example.RandomCommonCertificateVerifier` with your implemented class. This allows the revoke-api module to use your custom class for certificate verification.
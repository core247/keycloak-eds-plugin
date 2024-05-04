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
 * The CertificateMetadata class represents the metadata of a certificate.
 * It contains various attributes such as tax code, name, surname, and more.
 *
 * This class provides methods to set and retrieve the values of these attributes.
 */
public class CertificateMetadata {

    private String taxCode;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String birtDate;
    private String country;
    private String gender;
    private String notBefore;
    private String notAfter;
    private String dn;
    private String organization;
    private String bin;

    public CertificateMetadata() {}

    public CertificateMetadata withTaxCode(String taxCode) {
        this.taxCode = taxCode;
        return this;
    }

    public CertificateMetadata withName(String name) {
        this.name = name;
        return this;
    }

    public CertificateMetadata withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public CertificateMetadata withPatronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public CertificateMetadata withEmail(String email) {
        this.email = email;
        return this;
    }

    public CertificateMetadata withBirtDate(String birtDate) {
        this.birtDate = birtDate;
        return this;
    }

    public CertificateMetadata withCountry(String country) {
        this.country = country;
        return this;
    }

    public CertificateMetadata withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public CertificateMetadata withNotBefore(String notBefore) {
        this.notBefore = notBefore;
        return this;
    }

    public CertificateMetadata withNotAfter(String noteAfter) {
        this.notAfter = noteAfter;
        return this;
    }

    public CertificateMetadata withDn(String dn) {
        this.dn = dn;
        return this;
    }

    public CertificateMetadata withOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    public CertificateMetadata withBin(String bin) {
        this.bin = bin;
        return this;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getEmail() {
        return email;
    }

    public String getBirtDate() {
        return birtDate;
    }

    public String getCountry() {
        return country;
    }

    public String getGender() {
        return gender;
    }

    public String getNotBefore() {
        return notBefore;
    }

    public String getNotAfter() {
        return notAfter;
    }

    public String getDn() {
        return dn;
    }

    public String getOrganization() {
        return organization;
    }

    public String getBin() {
        return bin;
    }

    @Override
    public String toString() {
        return "CertificateMetadata{" +
                "taxCode='" + taxCode + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", birtDate='" + birtDate + '\'' +
                ", country='" + country + '\'' +
                ", gender='" + gender + '\'' +
                ", notBefore='" + notBefore + '\'' +
                ", notAfter='" + notAfter + '\'' +
                ", dn='" + dn + '\'' +
                ", organization='" + organization + '\'' +
                ", bin='" + bin + '\'' +
                '}';
    }
}

package com.phoenix.amazon.AmazonBackend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;


import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.AddressType;

@Entity
@Table(name = "user_address")
public class Address {
    @Id
    private String addressId;
    @Column(name = "user_mobile", nullable = false, unique = true)
    private String mobileNumber;
    @Column(name = "house_address_line_1", nullable = false)
    private String addressLineNumberOne;
    @Column(name = "house_address_line_2", nullable = false)
    private String addressLineNumberTwo;
    @Column(name = "address_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    @Column(name = "pin_code", nullable = false)
    private String pinCode;
    @Column(name = "latitude", nullable = false)
    private String latitude;
    @Column(name = "longitude", nullable = false)
    private String longitude;
    @Column(name = "town_or_city", nullable = false)
    private String townOrCity;
    @Column(name = "district", nullable = false)
    private String district;
    @Column(name = "state", nullable = false)
    private String state;
    @Column(name = "user_country", nullable = false)
    private String country;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    public Address() {
    }

    public Address(builder builder) {
        this.addressId = builder.addressId;
        this.mobileNumber = builder.mobileNumber;
        this.addressLineNumberOne = builder.addressLineNumberOne;
        this.addressLineNumberTwo = builder.addressLineNumberTwo;
        this.addressType = builder.addressType;
        this.pinCode = builder.pinCode;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.townOrCity = builder.townOrCity;
        this.district = builder.district;
        this.state = builder.state;
        this.users=builder.users;
        this.country = builder.country;
    }

    public static final class builder {
        private String addressId;
        private String mobileNumber;
        private String addressLineNumberOne;
        private String addressLineNumberTwo;
        private AddressType addressType;
        private String pinCode;
        private String latitude;
        private String longitude;
        private String townOrCity;
        private String district;
        private String state;
        private String country;
        private Users users;

        public builder() {
        }

        public builder addressId(final String addressId) {
            this.addressId = addressId;
            return this;
        }

        public builder mobileNumber(final String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public builder addressLineNumberOne(final String addressLineNumberOne) {
            this.addressLineNumberOne = addressLineNumberOne;
            return this;
        }

        public builder addressLineNumberTwo(final String addressLineNumberTwo) {
            this.addressLineNumberTwo = addressLineNumberTwo;
            return this;
        }

        public builder addressType(final AddressType addressType) {
            this.addressType = addressType;
            return this;
        }

        public builder pinCode(final String pinCode) {
            this.pinCode = pinCode;
            return this;
        }

        public builder latitude(final String latitude) {
            this.latitude = latitude;
            return this;
        }

        public builder longitude(final String longitude) {
            this.longitude = longitude;
            return this;
        }

        public builder townOrCity(final String townOrCity) {
            this.townOrCity = townOrCity;
            return this;
        }

        public builder district(final String district) {
            this.district = district;
            return this;
        }

        public builder state(final String state) {
            this.state = state;
            return this;
        }

        public builder country(final String country) {
            this.country = country;
            return this;
        }

        public builder users(final Users users) {
            this.users = users;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }

    public String getAddressId() {
        return addressId;
    }
    public String getMobileNumber() {
        return mobileNumber;
    }
    public String getAddressLineNumberOne() {
        return addressLineNumberOne;
    }
    public String getAddressLineNumberTwo() {return addressLineNumberTwo;}
    public AddressType getAddressType() {return addressType;}
    public Users getUsers() {return users;}
    public String getPinCode() {
        return pinCode;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getTownOrCity() {
        return townOrCity;
    }
    public String getDistrict() {
        return district;
    }
    public String getState() {
        return state;
    }
    public String getCountry() {
        return country;
    }

}
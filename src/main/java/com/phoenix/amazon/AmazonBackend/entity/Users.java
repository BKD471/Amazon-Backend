package com.phoenix.amazon.AmazonBackend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;

@Entity
@Table(name = "users")
public class Users extends Audit {
    @Id
    private String userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "user_primary_email", unique = true, nullable = false)
    private String primaryEmail;
    @Column(name = "user_secondary_email", unique = true)
    private String secondaryEmail;
    @Column(name = "user_password", length = 255, nullable = false)
    private String password;
    @Enumerated(value = EnumType.STRING)
    private GENDER gender;
    @Column(name = "user_image_name")
    private String profileImage;
    @Column(name = "last_seen")
    private LocalDateTime lastSeen;
    @Column(length = 1000)
    private String about;
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<PassWordSet> previous_password_set = new HashSet<>();
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<Address> address_set = new HashSet<>();

    public Users() {
    }

    public Users(builder builder) {
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.primaryEmail = builder.primaryEmail;
        this.secondaryEmail = builder.secondaryEmail;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.gender = builder.gender;
        this.profileImage = builder.profileImage;
        this.password = builder.password;
        this.previous_password_set = builder.previous_password_set;
        this.address_set=builder.addressSet;
        this.lastSeen = builder.lastSeen;
        this.about = builder.about;
        this.createdDate = builder.createdDate;
        this.createdBy = builder.createdBy;
    }

    public static final class builder {
        private String userId;
        private String userName;
        private String firstName;
        private String lastName;
        private String primaryEmail;
        private String secondaryEmail;
        private String password;
        private GENDER gender;
        private String profileImage;
        private LocalDateTime lastSeen;
        private String about;
        private Set<PassWordSet> previous_password_set;
        private Set<Address> addressSet;
        private LocalDate createdDate;
        private String createdBy;

        public builder() {
        }

        public builder userId(final String userId) {
            this.userId = userId;
            return this;
        }

        public builder userName(final String userName) {
            this.userName = userName;
            return this;
        }

        public builder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public builder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public builder primaryEmail(final String primaryEmail) {
            this.primaryEmail = primaryEmail;
            return this;
        }

        public builder secondaryEmail(final String secondaryEmail) {
            this.secondaryEmail = secondaryEmail;
            return this;
        }

        public builder password(final String password) {
            this.password = password;
            return this;
        }

        public builder previous_password_set(final Set<PassWordSet> previous_password_set) {
            this.previous_password_set = previous_password_set;
            return this;
        }

        public builder address_set(final Set<Address> addressSet) {
            this.addressSet = addressSet;
            return this;
        }

        public builder gender(final GENDER gender) {
            this.gender = gender;
            return this;
        }

        public builder profileImage(final String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public builder about(final String about) {
            this.about = about;
            return this;
        }

        public builder lastSeen(final LocalDateTime lastSeen) {
            this.lastSeen = lastSeen;
            return this;
        }

        public builder createdDate(final LocalDate createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public builder createdBy(final String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Users build() {
            return new Users(this);
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public String getPassword() {
        return password;
    }

    public Set<PassWordSet> getPrevious_password_set() {
        return previous_password_set;
    }

    public GENDER getGender() {
        return gender;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public String getAbout() {
        return about;
    }

    public Set<Address> getAddress_set() {return address_set;}
}

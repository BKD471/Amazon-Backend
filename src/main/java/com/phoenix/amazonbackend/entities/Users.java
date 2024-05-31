package com.phoenix.amazonbackend.entities;


import com.phoenix.amazonbackend.utils.AllConstants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
@Entity
@Table(name = "users")
public class Users extends Audit {
    @Id
    private UUID userId;

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
    private AllConstants.GENDER gender;

    @Column(name = "user_image_name")
    private String profileImage;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @Column(length = 1000)
    private String about;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<PassWordSet> previous_password_set = new LinkedHashSet<>();

    @Column(name = "last_modified")
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;
}

package com.wellness.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;              // _id

    private String name;          // _name

    @Column(unique = true, nullable = false)
    private String email;         // _email

    private String password;      // _password (hashed)

    @Enumerated(EnumType.STRING)
    private Role role;            // _role (PATIENT / PRACTITIONER)

    @Column(length = 1000)
    private String bio;           // _bio

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PractitionerProfile practitionerProfile;
}

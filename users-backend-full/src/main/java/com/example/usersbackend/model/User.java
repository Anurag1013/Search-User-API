package com.example.usersbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * This class represents a user entity.
 */
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
    private Long id;

    @NotBlank(message = "firstName must not be blank")
    private String firstName;

    @NotBlank(message = "lastName must not be blank")
    private String lastName;

    @NotBlank(message = "email must not be blank")
    @Email(message = "email must be valid")
    private String email;

    @Min(value = 0, message = "age must be >= 0")
    private Integer age;

    @Size(min = 3, message = "ssn must have at least 3 characters")
    private String ssn;

    private String role;

    @Pattern(regexp = "^[0-9\\-+() ]*$", message = "phone contains invalid characters")
    private String phone;

    @NotBlank(message = "username must not be blank")
    private String username;

    private String gender;

    @Column(length = 2000)
    private String addressJson;
}

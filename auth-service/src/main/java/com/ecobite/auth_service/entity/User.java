package com.ecobite.auth_service.entity;

import com.ecobite.auth_service.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String status;

    private boolean locked;

}

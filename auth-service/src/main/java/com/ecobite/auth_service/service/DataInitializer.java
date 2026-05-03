package com.ecobite.auth_service.service;

import com.ecobite.auth_service.entity.User;
import com.ecobite.auth_service.enums.Role;
import com.ecobite.auth_service.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {

        if(!repo.existsByUsername("admin")){

            User user = new User();

            user.setUsername("superadmin");
            user.setEmail("admin@test.com");
            user.setPassword(passwordEncoder.encode("test123"));
            user.setRole(Role.ADMIN);
            user.setStatus("ACTIVE");
            user.setLocked(false);
            user.setFailedAttempts(0);

            repo.save(user);

            System.out.println("✅ Default ADMIN user created");
        }
    }
}

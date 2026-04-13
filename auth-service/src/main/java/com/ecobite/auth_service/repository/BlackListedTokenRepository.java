package com.ecobite.auth_service.repository;

import com.ecobite.auth_service.entity.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken,Long> {
    boolean existsByToken(String token);
}

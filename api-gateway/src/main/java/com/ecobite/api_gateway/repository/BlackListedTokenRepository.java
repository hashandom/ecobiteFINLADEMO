package com.ecobite.api_gateway.repository;

import com.ecobite.api_gateway.entity.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken, Long>{
    boolean existsByToken(String token);
}

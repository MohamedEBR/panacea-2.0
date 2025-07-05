package com.example.panacea.repo;

import com.example.panacea.models.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {
    Optional<RevokedToken> findByToken(String token);
}

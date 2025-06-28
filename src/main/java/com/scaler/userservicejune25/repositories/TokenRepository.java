package com.scaler.userservicejune25.repositories;

import com.scaler.userservicejune25.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);

    Optional<Token> findByValueAndExpiryAtAfter(String value, Date currentTime);
    //select * from tokens where value = ? and expiry_at > currentTime
}

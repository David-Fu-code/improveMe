package com.drikek.improveMe.service.token;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<@NonNull ConfirmationToken, @NonNull Long> {

    Optional<ConfirmationToken> findByToken(String token);
}

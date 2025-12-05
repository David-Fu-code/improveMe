package com.drikek.improveMe.repository;

import com.drikek.improveMe.entity.ActiveSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActiveSessionRepository extends JpaRepository<ActiveSession, Long> {
    List<ActiveSession> findByUserId(Long userId);

    Optional<ActiveSession> findByJwtToken(String jwtToken);
}

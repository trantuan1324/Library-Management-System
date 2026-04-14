package com.rabbyte.librarymanagementsystem.repositories;

import com.rabbyte.librarymanagementsystem.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.user.id = :userId")
    void deleteAllByUserId(Long userId);

    @Modifying
    @Query("UPDATE Session s SET s.revoked = true WHERE s.user.id = :userId")
    void revokeAllByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.refreshToken = :refreshToken")
    void deleteByRefreshToken(String refreshToken);


}

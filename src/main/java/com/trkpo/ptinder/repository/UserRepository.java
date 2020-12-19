package com.trkpo.ptinder.repository;

import com.trkpo.ptinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByGoogleId(String googleId);
    List<User> findByAddress(String address);
    boolean existsByGoogleId(String googleId);
}

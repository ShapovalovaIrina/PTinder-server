package com.trkpo.ptinder.repository;

import com.trkpo.ptinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

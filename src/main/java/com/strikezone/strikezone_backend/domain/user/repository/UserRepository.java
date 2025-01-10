package com.strikezone.strikezone_backend.domain.user.repository;

import com.strikezone.strikezone_backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    User findByUsername(String username);

}

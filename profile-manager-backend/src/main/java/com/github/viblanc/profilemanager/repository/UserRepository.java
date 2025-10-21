package com.github.viblanc.profilemanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.viblanc.profilemanager.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

}

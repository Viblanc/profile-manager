package com.github.viblanc.profilemanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.viblanc.profilemanager.entity.UserType;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
	Optional<UserType> findByName(String name);
}

package com.github.viblanc.profilemanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.github.viblanc.profilemanager.entity.User;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	List<User> findByUserTypeName(String type);
}

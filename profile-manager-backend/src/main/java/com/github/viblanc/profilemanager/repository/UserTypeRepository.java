package com.github.viblanc.profilemanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.github.viblanc.profilemanager.entity.UserType;

@RepositoryRestResource(path = "user_types", collectionResourceRel = "user_types")
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
	Optional<UserType> findByName(String name);
}

package com.github.viblanc.profilemanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.github.viblanc.profilemanager.entity.User;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByUserTypeName(String type);
}

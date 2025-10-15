package com.github.viblanc.profilemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.github.viblanc.profilemanager.entity.User;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
}

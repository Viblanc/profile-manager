package com.github.viblanc.profilemanager.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_type")
public class UserType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "userType", cascade = CascadeType.ALL)
	private Set<User> users;

	public UserType() {
	}

	public UserType(Long id, String name, Set<User> users) {
		this.id = id;
		this.name = name;
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void addUser(User user) {
		if (Objects.nonNull(user)) {
			if (Objects.isNull(this.users)) {
				this.users = new HashSet<>();
			}

			this.users.add(user);
			user.setUserType(this);
		}
	}

	public void removeUser(User user) {
		if (Objects.nonNull(user)) {
			if (Objects.isNull(this.users)) {
				return;
			}

			this.users.remove(user);
			user.setUserType(null);
		}
	}

}

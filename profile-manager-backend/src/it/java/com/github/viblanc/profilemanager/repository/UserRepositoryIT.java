package com.github.viblanc.profilemanager.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.github.viblanc.profilemanager.config.IntegrationTestConfig;
import com.github.viblanc.profilemanager.entity.User;
import com.github.viblanc.profilemanager.entity.UserType;

@Import(IntegrationTestConfig.class)
@DataJpaTest
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByEmail() {
        UserType userType = new UserType(null, "Admin", null);
        entityManager.persist(userType);

        User user = new User(null, "John", "Doe", "john@doe.mail", userType);
        entityManager.persist(user);

        Optional<User> actual = userRepository.findByEmail("john@doe.mail");

        assertThat(actual).isPresent().hasValue(user);
    }

}

package com.github.viblanc.profilemanager.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import com.github.viblanc.profilemanager.config.IntegrationTestConfig;
import com.github.viblanc.profilemanager.entity.UserType;

@Import(IntegrationTestConfig.class)
@DataJpaTest
class UserTypeRepositoryIT {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByName() {
        UserType userType = new UserType(null, "Admin", null);
        entityManager.persist(userType);

        Optional<UserType> actual = userTypeRepository.findByName("Admin");

        assertThat(actual).isPresent().hasValue(userType);
    }

}

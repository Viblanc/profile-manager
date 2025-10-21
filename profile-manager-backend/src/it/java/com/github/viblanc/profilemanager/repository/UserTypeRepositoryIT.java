package com.github.viblanc.profilemanager.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import com.github.viblanc.profilemanager.config.MyTestConfiguration;
import com.github.viblanc.profilemanager.entity.UserType;

@Import(MyTestConfiguration.class)
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
        assertAll(() -> assertEquals(true, actual.isPresent()), () -> assertEquals(userType, actual.get()));
    }

}

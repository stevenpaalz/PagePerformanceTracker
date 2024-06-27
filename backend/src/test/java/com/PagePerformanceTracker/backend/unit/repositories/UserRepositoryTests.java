package com.PagePerformanceTracker.backend.unit.repositories;

import com.PagePerformanceTracker.backend.models.User;
import com.PagePerformanceTracker.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserRepository mockUserRepository;

    @Test
    void testFindByEmail() {

        String email = "test@example.com";
        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .build();

        when(mockUserRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        Optional<User> found = userRepository.findByEmail(email);
        assertEquals(user.getEmail(), found.orElseThrow().getEmail());
    }

}

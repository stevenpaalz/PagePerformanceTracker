package com.PagePerformanceTracker.backend.unit.models;

import com.PagePerformanceTracker.backend.models.User;
import com.PagePerformanceTracker.backend.models.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {
        private User user;

        @BeforeEach
        void setUp() {
            user = User.builder()
                    .id(1L)
                    .firstName("John")
                    .lastName("Doe")
                    .email("john.doe@example.com")
                    .password("password")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .role(Role.ROLE_USER)
                    .build();
        }

        @Test
        void testGetterSetter() {
            assertEquals(1L, user.getId());
            assertEquals("John", user.getFirstName());
            assertEquals("Doe", user.getLastName());
            assertEquals("john.doe@example.com", user.getEmail());
            assertEquals("password", user.getPassword());
            assertEquals(Role.ROLE_USER, user.getRole());
        }

        @Test
        void testGetAuthorities() {
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            assertEquals(1, authorities.size());
            assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());
        }

        @Test
        void testUserDetailsMethods() {
            UserDetails userDetails = new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return List.of(new SimpleGrantedAuthority(user.getRole().name()));
                }

                @Override
                public String getPassword() {
                    return user.getPassword();
                }

                @Override
                public String getUsername() {
                    return user.getEmail();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };

            assertEquals("john.doe@example.com", userDetails.getUsername());
            assertEquals("password", userDetails.getPassword());
            assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
        }
}

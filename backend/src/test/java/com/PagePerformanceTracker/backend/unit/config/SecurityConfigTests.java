package com.PagePerformanceTracker.backend.unit.config;

import com.PagePerformanceTracker.backend.config.SecurityConfig;
import com.PagePerformanceTracker.backend.security.JwtAuthenticationFilter;
import com.PagePerformanceTracker.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class SecurityConfigTests {
    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticationProvider() {
        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        when(userService.userDetailsService()).thenReturn(mockUserDetailsService);

        AuthenticationProvider authProvider = securityConfig.authenticationProvider();
        assertNotNull(authProvider);

        verify(userService).userDetailsService();
    }
}
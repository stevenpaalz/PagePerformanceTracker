package com.PagePerformanceTracker.backend.unit.service;

import com.PagePerformanceTracker.backend.dto.JwtAuthenticationResponse;
import com.PagePerformanceTracker.backend.dto.SignInRequest;
import com.PagePerformanceTracker.backend.dto.SignUpRequest;
import com.PagePerformanceTracker.backend.models.Role;
import com.PagePerformanceTracker.backend.models.User;
import com.PagePerformanceTracker.backend.service.AuthenticationService;
import com.PagePerformanceTracker.backend.service.JwtService;
import com.PagePerformanceTracker.backend.service.UserService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.PagePerformanceTracker.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


public class AuthenticationServiceTests {

        @Mock
        private UserRepository userRepository;

        @Mock
        private UserService userService;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private JwtService jwtService;

        @Mock
        private AuthenticationManager authenticationManager;

        @InjectMocks
        private AuthenticationService authenticationService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testSignup() {
            SignUpRequest request = new SignUpRequest();
            request.setFirstName("John");
            request.setLastName("Doe");
            request.setEmail("john.doe@example.com");
            request.setPassword("password");

            User user = User.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("john.doe@example.com")
                    .password("encodedPassword")
                    .role(Role.ROLE_USER)
                    .build();

            when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
            when(userService.save(any(User.class))).thenReturn(user);
            when(jwtService.generateToken(user)).thenReturn("jwtToken");

            JwtAuthenticationResponse response = authenticationService.signup(request);

            assertNotNull(response);
            assertEquals("jwtToken", response.getToken());
            verify(userService, times(1)).save(any(User.class));
        }

        @Test
        public void testSignin() {
            SignInRequest request = new SignInRequest();
            request.setEmail("john.doe@example.com");
            request.setPassword("password");

            User user = User.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("john.doe@example.com")
                    .password("encodedPassword")
                    .role(Role.ROLE_USER)
                    .build();

            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
            when(jwtService.generateToken(user)).thenReturn("jwtToken");

            JwtAuthenticationResponse response = authenticationService.signin(request);

            assertNotNull(response);
            assertEquals("jwtToken", response.getToken());
            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(userRepository, times(1)).findByEmail(request.getEmail());
        }

        @Test
        public void testSigninInvalidEmailOrPassword() {
            SignInRequest request = new SignInRequest();
            request.setEmail("invalid@example.com");
            request.setPassword("password");

            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> {
                authenticationService.signin(request);
            });

            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(userRepository, times(1)).findByEmail(request.getEmail());
        }

}

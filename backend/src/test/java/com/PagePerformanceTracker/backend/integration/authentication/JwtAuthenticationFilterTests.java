package com.PagePerformanceTracker.backend.integration.authentication;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.PagePerformanceTracker.backend.security.JwtAuthenticationFilter;
import com.PagePerformanceTracker.backend.service.JwtService;
import com.PagePerformanceTracker.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(JwtAuthenticationFilterTests.TestController.class)
public class JwtAuthenticationFilterTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @Mock
    private UserDetailsService userDetailsService;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userService.userDetailsService()).thenReturn(userDetailsService);

        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userService);

        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .addFilters(jwtAuthenticationFilter)
                .build();
    }

    @Test
    public void testDoFilterInternal_NoAuthHeader() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk());

        verify(jwtService, never()).extractUserName(anyString());
    }

    @Test
    public void testDoFilterInternal_InvalidAuthHeader() throws Exception {
        mockMvc.perform(get("/test").header("Authorization", "InvalidHeader"))
                .andExpect(status().isOk());

        verify(jwtService, never()).extractUserName(anyString());
    }

    @Test
    public void testDoFilterInternal_ValidAuthHeader_ValidToken() throws Exception {
        String jwt = "valid.jwt.token";
        String userEmail = "user@example.com";
        UserDetails userDetails = mock(UserDetails.class);

        when(jwtService.extractUserName(jwt)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwt, userDetails)).thenReturn(true);

        mockMvc.perform(get("/test").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());

        verify(jwtService, times(1)).extractUserName(jwt);
        verify(userService, times(1)).userDetailsService();
        verify(userDetailsService, times(1)).loadUserByUsername(userEmail);
        verify(jwtService, times(1)).isTokenValid(jwt, userDetails);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_ValidAuthHeader_InvalidToken() throws Exception {
        String jwt = "valid.jwt.token";
        String userEmail = "user@example.com";
        UserDetails userDetails = mock(UserDetails.class);

        when(jwtService.extractUserName(jwt)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwt, userDetails)).thenReturn(false);

        mockMvc.perform(get("/test").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());

        verify(jwtService, times(1)).extractUserName(jwt);
        verify(userService, times(1)).userDetailsService();
        verify(userDetailsService, times(1)).loadUserByUsername(userEmail);
        verify(jwtService, times(1)).isTokenValid(jwt, userDetails);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @RestController
    public static class TestController {
        @GetMapping("/test")
        public ResponseEntity<String> testEndpoint() {
            return ResponseEntity.ok("Test endpoint");
        }
    }
}
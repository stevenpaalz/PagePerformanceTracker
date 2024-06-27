package com.PagePerformanceTracker.backend.unit.dto;

import com.PagePerformanceTracker.backend.dto.JwtAuthenticationResponse;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JwtAuthenticationResponseTests {

    @Test
    public void testGetterAndSetter() {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken("testToken");

        assertEquals("testToken", response.getToken());
    }

    @Test
    public void testAllArgsConstructor() {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("testToken");

        assertEquals("testToken", response.getToken());
    }

    @Test
    public void testNoArgsConstructor() {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();

        assertNull(response.getToken());
    }

    @Test
    public void testBuilder()  {
        JwtAuthenticationResponse response = JwtAuthenticationResponse.builder()
                .token("testToken")
                .build();

        assertEquals("testToken", response.getToken());
    }
}

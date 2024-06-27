package com.PagePerformanceTracker.backend.unit.models;

import com.PagePerformanceTracker.backend.models.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleTests {

    @Test
    void testRoleEnumValues() {
        assertEquals("ROLE_ADMIN", Role.ROLE_ADMIN.name());
        assertEquals("ROLE_USER", Role.ROLE_USER.name());
    }
}

package com.simplesdental.product.Enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleTest {

    @Test
    void testEnumValues() {
        assertEquals("ADMIN", Role.ADMIN.name());
        assertEquals("USER", Role.USER.name());
    }

    @Test
    void testValueOf() {
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        assertEquals(Role.USER, Role.valueOf("USER"));
    }

    @Test
    void testInvalidValueOf() {
        assertThrows(IllegalArgumentException.class, () -> Role.valueOf("INVALID"));
    }
}

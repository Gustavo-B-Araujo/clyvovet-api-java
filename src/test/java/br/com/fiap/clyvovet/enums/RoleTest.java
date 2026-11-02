package br.com.fiap.clyvovet.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void deveConterTutorEVeterinario() {
        assertEquals(2, Role.values().length);
        assertEquals(Role.TUTOR, Role.valueOf("TUTOR"));
        assertEquals(Role.VETERINARIO, Role.valueOf("VETERINARIO"));
    }
}

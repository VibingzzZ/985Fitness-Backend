package com.fitness985.fitnesssecurity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LoginPrincipalTest {

    @Test
    void shouldExposeConstructorArgumentsThroughAccessors() {
        LoginPrincipal principal = new LoginPrincipal(1L, "alice", Set.of("USER", "ADMIN"));

        assertThat(principal.userId()).isEqualTo(1L);
        assertThat(principal.username()).isEqualTo("alice");
        assertThat(principal.roles()).containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @Test
    void shouldReplaceNullRolesWithEmptySet() {
        LoginPrincipal principal = new LoginPrincipal(1L, "alice", null);

        assertThat(principal.roles()).isNotNull().isEmpty();
    }

    @Test
    void shouldCopyRolesSoExternalMutationDoesNotAffectPrincipal() {
        Set<String> mutableRoles = new HashSet<>(Set.of("USER"));

        LoginPrincipal principal = new LoginPrincipal(1L, "alice", mutableRoles);
        mutableRoles.add("ADMIN");

        assertThat(principal.roles()).containsExactly("USER");
    }

    @Test
    void rolesShouldBeImmutable() {
        LoginPrincipal principal = new LoginPrincipal(1L, "alice", Set.of("USER"));

        assertThatThrownBy(() -> principal.roles().add("ADMIN"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldDeduplicateRepeatedRoles() {
        LoginPrincipal principal = new LoginPrincipal(1L, "alice", Set.of("USER", "USER"));

        assertThat(principal.roles()).containsExactly("USER");
    }

    @Test
    void equalsAndHashCodeShouldBeBasedOnAllComponents() {
        LoginPrincipal first = new LoginPrincipal(1L, "alice", Set.of("USER"));
        LoginPrincipal second = new LoginPrincipal(1L, "alice", Set.of("USER"));

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void equalsShouldReturnFalseWhenUserIdDiffers() {
        LoginPrincipal first = new LoginPrincipal(1L, "alice", Set.of("USER"));
        LoginPrincipal second = new LoginPrincipal(2L, "alice", Set.of("USER"));

        assertThat(first).isNotEqualTo(second);
    }
}
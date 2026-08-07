package com.fitness985.fitnesssecurity.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class JwtPropertiesTest {

    @Test
    void shouldExposeConstructorArgumentsThroughAccessors() {
        JwtProperties properties =
                new JwtProperties("secret-value", Duration.ofHours(2), Duration.ofDays(30));

        assertThat(properties.secret()).isEqualTo("secret-value");
        assertThat(properties.accessTokenExpiration()).isEqualTo(Duration.ofHours(2));
        assertThat(properties.refreshTokenExpiration()).isEqualTo(Duration.ofDays(30));
    }

    @Test
    void equalsAndHashCodeShouldBeBasedOnAllComponents() {
        JwtProperties first = new JwtProperties("secret", Duration.ofHours(2), Duration.ofDays(30));
        JwtProperties second = new JwtProperties("secret", Duration.ofHours(2), Duration.ofDays(30));

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void equalsShouldReturnFalseWhenSecretDiffers() {
        JwtProperties first = new JwtProperties("secret-a", Duration.ofHours(2), Duration.ofDays(30));
        JwtProperties second = new JwtProperties("secret-b", Duration.ofHours(2), Duration.ofDays(30));

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void shouldAllowNullComponentsSincePropertyBindingMayOmitThem() {
        JwtProperties properties = new JwtProperties(null, null, null);

        assertThat(properties.secret()).isNull();
        assertThat(properties.accessTokenExpiration()).isNull();
        assertThat(properties.refreshTokenExpiration()).isNull();
    }
}
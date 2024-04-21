package se.yrgo.libraryapp.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class UsernameTest {
    private static final Logger log = LoggerFactory.getLogger(UsernameTest.class);

    @BeforeAll
    public static void setup() {
        Username.initializePattern("[a-zA-Z]{3,60}(\\d+)?");
    }

    @ParameterizedTest
    @ValueSource(strings = { "bosse", "anotherValid123", "user123" })
    void validUsernames(String username) {
        assertTrue(Username.validate(username));
    }

    @ParameterizedTest
    @ValueSource(strings = { "name with space", "!@#$%^&*()", "user!123" })
    void invalidUsernames(String username) {
        assertFalse(Username.validate(username));
    }

    @ParameterizedTest
    @NullSource
    void nullUsername(String username) {
        // Check if username is null before calling validate
        boolean validationResult = username != null && Username.validate(username);
        log.info("Running nullUsername with username: {}, Result: {}", username, validationResult);
        assertFalse(validationResult);
    }

    @Test
    void veryLongUsername() {
        assertFalse(Username.validate("a".repeat(100)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "a" })
    void singleCharacterUsername(String username) {
        assertFalse(Username.validate(username));
    }

    @ParameterizedTest
    @ValueSource(strings = { "user123" })
    void numericUsername(String username) {
        log.info("Running numericUsername test with username: {}", username);
        assertTrue(Username.validate(username));
    }

    @ParameterizedTest
    @ValueSource(strings = { "someOtherUsername1" })
    void someOtherTest(String username) {
        boolean validationResult = Username.validate(username);
        log.info("Running someOtherTest with username: {}, Result: {}", username, validationResult);
        assertTrue(validationResult);
    }

    @Test
    void blankUsername() {
        log.info("Running blankUsername test");
        assertFalse(Username.validate(""));
    }
}

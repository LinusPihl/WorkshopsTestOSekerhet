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

public class RealNameTest {
    private static final Logger log = LoggerFactory.getLogger(RealNameTest.class);

    @BeforeAll
    public static void setup() {
        RealName.initializePattern("^(?=.* )[A-Za-z- ]{2,50}$");
    }

    @ParameterizedTest
    @ValueSource(strings = { "John Doe", "Jane Smith", "Alice-Marie Johnson" })
    void validRealNames(String name) {
        assertTrue(RealName.validate(name));
    }

    @ParameterizedTest
    @ValueSource(strings = { "123", "John123", "Mary_White", "Alice@Marie", "Robert;DropTable" })
    void invalidRealNames(String name) {
        assertFalse(RealName.validate(name));
    }

    @ParameterizedTest
    @NullSource
    void nullRealName(String name) {
        // Check if name is null before calling validate
        boolean validationResult = name != null && RealName.validate(name);
        log.info("Running nullRealName with name: {}, Result: {}", name, validationResult);
        assertFalse(validationResult);
    }

    @Test
    void veryLongRealName() {
        assertFalse(RealName.validate("a".repeat(100)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "a" })
    void singleCharacterRealName(String name) {
        assertFalse(RealName.validate(name));
    }
}

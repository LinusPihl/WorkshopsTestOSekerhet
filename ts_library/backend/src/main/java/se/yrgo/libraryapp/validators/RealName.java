package se.yrgo.libraryapp.validators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This validator checks that the real names match our high standard for proper names. I.e. no bad
 * words.
 */
public final class RealName {
    private static Logger logger = LoggerFactory.getLogger(RealName.class);
    private static final Set<String> invalidWords = new HashSet<>();
    private static Pattern namePattern;

    static {
        try (InputStream is = RealName.class.getClassLoader().getResourceAsStream("bad_words.txt");
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            while (reader.readLine() != null) {
                invalidWords.addAll(reader.lines().collect(Collectors.toSet()));
            }
        } catch (IOException ex) {
            logger.error("Unable to initialize list of bad words", ex);
        }
    }

    private RealName() {}

    /**
     * Initializes the pattern for validating real names.
     * 
     * @param pattern the regular expression pattern
     */
    public static void initializePattern(String pattern) {
        namePattern = Pattern.compile(pattern);
    }

    /**
     * Validates if the given name is a valid and proper name.
     * 
     * @param name the name to check
     * @return true if valid, false if not
     * 
     */
    public static boolean validate(String name) {
        if (namePattern == null) {
            throw new IllegalStateException("Name pattern is not initialized. Call initializePattern() first.");
        }
        String cleanName = Utils.cleanAndUnLeet(name);
        String[] words = cleanName.split("\\W+");
        for (int i = 1; i < words.length; i++) {
            if (invalidWords.contains(words[i])) {
                return false;
            }
        }
        return namePattern.matcher(cleanName).matches();
    }
}

package io.itch.frogcheese.sharecart._test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A set of utilities for testing
 */
public class Utils {

    private Utils() {

    }

    /**
     * Constructs a StringBuilder containing the given amount of the given character.
     *
     * @param length          the desired length of the StringBuilder
     * @param fillerCharacter the character that should be used to fill the StringBuilder to the desired length
     *
     * @return A new StringBuilder consisting entirely of <em>"length"</em> number of <em>"fillerCharacter"</em>.
     */
    public static StringBuilder getStringBuilderWithLength(int length, char fillerCharacter) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(fillerCharacter);
        } while (sb.length() < length);

        assertThat(sb.toString()).hasSize(length);

        return sb;
    }
}

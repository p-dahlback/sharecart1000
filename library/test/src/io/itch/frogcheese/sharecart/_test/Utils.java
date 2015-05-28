package io.itch.frogcheese.sharecart._test;

import static org.assertj.core.api.Assertions.assertThat;

public class Utils {

    private Utils() {

    }

    public static StringBuilder getStringBuilderWithLength(int length, char fillerCharacter) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(fillerCharacter);
        } while (sb.length() < length);

        assertThat(sb.toString()).hasSize(length);
        
        return sb;
    }
}

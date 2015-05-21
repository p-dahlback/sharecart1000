package io.itch.frogcheese.sharecart._test;

public class Utils {

    private Utils() {

    }

    public static StringBuilder getStringBuilderWithLength(int length, char fillerCharacter) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(fillerCharacter);
        } while (sb.length() < length);
        return sb;
    }
}

package io.itch.frogcheese.sharecart;

import io.itch.frogcheese.sharecart._test.Utils;
import org.junit.Test;

import static io.itch.frogcheese.sharecart.Constraints.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ConstraintsTest {

    @Test
    public void testConstraint_values_are_correct() throws Exception {
        assertThat(MAX_SIZE_X).isEqualTo(1023);
        assertThat(MAX_SIZE_Y).isEqualTo(1023);

        assertThat(MISC_ITEMS_LENGTH).isEqualTo(4);
        assertThat(MAX_SIZE_MISC).isEqualTo(65535);

        assertThat(MAX_SIZE_NAME_LENGTH).isEqualTo(1023);

        assertThat(SWITCH_ITEMS_LENGTH).isEqualTo(8);
    }

    @Test
    public void testValidX() throws Exception {
        assertThat(validX(0)).isTrue();
        assertThat(validX(10)).isTrue();
        assertThat(validX(MAX_SIZE_X)).isTrue();
        assertThat(validX(-1)).isFalse();
        assertThat(validX(MAX_SIZE_X + 1)).isFalse();
    }

    @Test
    public void testValidY() throws Exception {
        assertThat(validY(0)).isTrue();
        assertThat(validY(10)).isTrue();
        assertThat(validY(MAX_SIZE_Y)).isTrue();
        assertThat(validY(-1)).isFalse();
        assertThat(validY(MAX_SIZE_Y + 1)).isFalse();
    }

    @Test
    public void testValidMisc() throws Exception {
        assertThat(validMisc(0)).isTrue();
        assertThat(validMisc(10)).isTrue();
        assertThat(validMisc(MAX_SIZE_MISC)).isTrue();
        assertThat(validMisc(-1)).isFalse();
        assertThat(validMisc(MAX_SIZE_MISC + 1)).isFalse();
    }

    @Test
    public void testValidMisc_index() throws Exception {
        assertThat(validMiscIndex(0)).isTrue();
        assertThat(validMiscIndex(2)).isTrue();
        assertThat(validMiscIndex(MISC_ITEMS_LENGTH - 1)).isTrue();
        assertThat(validMiscIndex(-1)).isFalse();
        assertThat(validMiscIndex(MISC_ITEMS_LENGTH)).isFalse();
    }

    @Test
    public void testValidName() throws Exception {
        assertThat(validName("")).isTrue();
        assertThat(validName("A name this Be/ 1 - 2 - 3!")).isTrue();
        assertThat(validName(null)).isFalse();

        StringBuilder sb = Utils.getStringBuilderWithLength(MAX_SIZE_NAME_LENGTH, 'a');

        assertThat(validName(sb.toString())).isTrue();

        sb.append('a');

        assertThat(validName(sb.toString())).isFalse();
    }

    @Test
    public void testValidSwitch_index() throws Exception {
        assertThat(validSwitchIndex(0)).isTrue();
        assertThat(validSwitchIndex(2)).isTrue();
        assertThat(validSwitchIndex(SWITCH_ITEMS_LENGTH - 1)).isTrue();
        assertThat(validSwitchIndex(-1)).isFalse();
        assertThat(validSwitchIndex(SWITCH_ITEMS_LENGTH)).isFalse();
    }
}

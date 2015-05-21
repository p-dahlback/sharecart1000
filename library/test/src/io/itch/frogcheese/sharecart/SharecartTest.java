package io.itch.frogcheese.sharecart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class SharecartTest {

    private Sharecart sharecart;

    @Before
    public void setUp() throws Exception {
        sharecart = new Sharecart();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testPreconditions() throws Exception {
        sharecart = new Sharecart();

        assertThat(sharecart).isNotNull();
        assertThat(sharecart.x()).isZero();
        assertThat(sharecart.y()).isZero();

        assertThat(sharecart.miscLength()).isEqualTo(Constraints.MISC_ITEMS_LENGTH);
        for (int i = 0; i < sharecart.miscLength(); i++) {
            assertThat(sharecart.misc(i)).isZero();
        }

        assertThat(sharecart.name()).isEqualTo(Sharecart.DEFAULT_NAME);

        assertThat(sharecart.switchLength()).isEqualTo(Constraints.SWITCH_ITEMS_LENGTH);
        for (int i = 0; i < sharecart.switchLength(); i++) {
            assertThat(sharecart.switchValue(i)).isFalse();
        }
    }

    @Test
    public void testConstructor_from_defaults() throws Exception {
        sharecart = Sharecart.withDefaults();

        assertThat(sharecart).isNotNull();
        assertThat(sharecart.x()).isZero();
        assertThat(sharecart.y()).isZero();

        assertThat(sharecart.miscLength()).isEqualTo(Constraints.MISC_ITEMS_LENGTH);
        for (int i = 0; i < sharecart.miscLength(); i++) {
            assertThat(sharecart.misc(i)).isZero();
        }

        assertThat(Sharecart.DEFAULT_NAME).isEqualTo("CHANGEME");
        assertThat(sharecart.name()).isEqualTo(Sharecart.DEFAULT_NAME);

        assertThat(sharecart.switchLength()).isEqualTo(Constraints.SWITCH_ITEMS_LENGTH);
        for (int i = 0; i < sharecart.switchLength(); i++) {
            assertThat(sharecart.switchValue(i)).isFalse();
        }
    }

    @Test
    public void testX_values() throws Exception {
        sharecart.x(100);
        assertThat(sharecart.x()).isEqualTo(100);

        sharecart.x(0);
        assertThat(sharecart.x()).isEqualTo(0);

        sharecart.x(Constraints.MAX_SIZE_X);
        assertThat(sharecart.x()).isEqualTo(Constraints.MAX_SIZE_X);
    }

    @Test
    public void testY_values() throws Exception {
        sharecart.y(100);
        assertThat(sharecart.y()).isEqualTo(100);

        sharecart.y(0);
        assertThat(sharecart.y()).isEqualTo(0);

        sharecart.y(Constraints.MAX_SIZE_Y);
        assertThat(sharecart.y()).isEqualTo(Constraints.MAX_SIZE_Y);
    }

    @Test
    public void testMisc_index_constraints() throws Exception {

        try {
            sharecart.misc(-1);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            sharecart.misc(Constraints.MISC_ITEMS_LENGTH);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            sharecart.misc(-1, 0);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            sharecart.misc(Constraints.MISC_ITEMS_LENGTH, 0);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        sharecart.misc(0);
        sharecart.misc(Constraints.MISC_ITEMS_LENGTH - 1);

        sharecart.misc(0, 0);
        sharecart.misc(Constraints.MISC_ITEMS_LENGTH - 1, 0);
    }

    @Test
    public void testMisc_value_set_to_correct_index() throws Exception {
        sharecart.misc(0, 20);

        assertThat(sharecart.misc(0)).isEqualTo(20);
        assertThat(sharecart.misc(1)).isZero();
        assertThat(sharecart.misc(2)).isZero();
        assertThat(sharecart.misc(3)).isZero();

        sharecart.misc(1, 44);

        assertThat(sharecart.misc(0)).isEqualTo(20);
        assertThat(sharecart.misc(1)).isEqualTo(44);
        assertThat(sharecart.misc(2)).isZero();
        assertThat(sharecart.misc(3)).isZero();

        sharecart.misc(2, 67);

        assertThat(sharecart.misc(0)).isEqualTo(20);
        assertThat(sharecart.misc(1)).isEqualTo(44);
        assertThat(sharecart.misc(2)).isEqualTo(67);
        assertThat(sharecart.misc(3)).isZero();

        sharecart.misc(3, 12);

        assertThat(sharecart.misc(0)).isEqualTo(20);
        assertThat(sharecart.misc(1)).isEqualTo(44);
        assertThat(sharecart.misc(2)).isEqualTo(67);
        assertThat(sharecart.misc(3)).isEqualTo(12);

        sharecart.misc(1, 0);
        sharecart.misc(3, 0);

        assertThat(sharecart.misc(0)).isEqualTo(20);
        assertThat(sharecart.misc(1)).isZero();
        assertThat(sharecart.misc(2)).isEqualTo(67);
        assertThat(sharecart.misc(3)).isZero();
    }

    @Test
    public void testMisc_values() throws Exception {
        sharecart.misc(0, 20);
        assertThat(sharecart.misc(0)).isEqualTo(20);

        sharecart.misc(0, 0);
        assertThat(sharecart.misc(0)).isEqualTo(0);

        sharecart.misc(0, Constraints.MAX_SIZE_MISC);
        assertThat(sharecart.misc(0)).isEqualTo(Constraints.MAX_SIZE_MISC);
    }

    @Test
    public void testName_values() throws Exception {
        sharecart.name("a name");
        assertThat(sharecart.name()).isEqualTo("a name");

        sharecart.name(null);
        assertThat(sharecart.name()).isNull();

        sharecart.name("");
        assertThat(sharecart.name()).isEmpty();

        sharecart.name("1234567");
        assertThat(sharecart.name()).isEqualTo("1234567");
    }

    @Test
    public void testSwitch_index_constraints() throws Exception {

        try {
            sharecart.switchValue(-1);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            sharecart.switchValue(Constraints.SWITCH_ITEMS_LENGTH);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            sharecart.switchValue(-1, false);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            sharecart.switchValue(Constraints.SWITCH_ITEMS_LENGTH, false);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        sharecart.switchValue(0);
        sharecart.switchValue(Constraints.SWITCH_ITEMS_LENGTH - 1);

        sharecart.switchValue(0, false);
        sharecart.switchValue(Constraints.SWITCH_ITEMS_LENGTH - 1, false);
    }

    @Test
    public void testSwitch_value_set_to_correct_index() throws Exception {
        sharecart.switchValue(0, true);
        assertThat(sharecart.switchValue(0)).isTrue();
        assertThat(sharecart.switchValue(1)).isFalse();
        assertThat(sharecart.switchValue(2)).isFalse();
        assertThat(sharecart.switchValue(3)).isFalse();
        assertThat(sharecart.switchValue(4)).isFalse();
        assertThat(sharecart.switchValue(5)).isFalse();
        assertThat(sharecart.switchValue(6)).isFalse();
        assertThat(sharecart.switchValue(7)).isFalse();

        sharecart.switchValue(1, true);
        assertThat(sharecart.switchValue(0)).isTrue();
        assertThat(sharecart.switchValue(1)).isTrue();
        assertThat(sharecart.switchValue(2)).isFalse();
        assertThat(sharecart.switchValue(3)).isFalse();
        assertThat(sharecart.switchValue(4)).isFalse();
        assertThat(sharecart.switchValue(5)).isFalse();
        assertThat(sharecart.switchValue(6)).isFalse();
        assertThat(sharecart.switchValue(7)).isFalse();

        sharecart.switchValue(5, true);
        assertThat(sharecart.switchValue(0)).isTrue();
        assertThat(sharecart.switchValue(1)).isTrue();
        assertThat(sharecart.switchValue(2)).isFalse();
        assertThat(sharecart.switchValue(3)).isFalse();
        assertThat(sharecart.switchValue(4)).isFalse();
        assertThat(sharecart.switchValue(5)).isTrue();
        assertThat(sharecart.switchValue(6)).isFalse();
        assertThat(sharecart.switchValue(7)).isFalse();

        sharecart.switchValue(1, false);
        assertThat(sharecart.switchValue(0)).isTrue();
        assertThat(sharecart.switchValue(1)).isFalse();
        assertThat(sharecart.switchValue(2)).isFalse();
        assertThat(sharecart.switchValue(3)).isFalse();
        assertThat(sharecart.switchValue(4)).isFalse();
        assertThat(sharecart.switchValue(5)).isTrue();
        assertThat(sharecart.switchValue(6)).isFalse();
        assertThat(sharecart.switchValue(7)).isFalse();
    }

    @Test
    public void testSwitch_values() throws Exception {
        sharecart.switchValue(0, true);
        assertThat(sharecart.switchValue(0)).isTrue();

        sharecart.switchValue(0, false);
        assertThat(sharecart.switchValue(0)).isFalse();
    }

    @Test
    public void testEquals_requires_all_parameters_to_be_equal() throws Exception {
        Sharecart defaultCart = Sharecart.withDefaults();

        assertThat(sharecart).isEqualTo(defaultCart);
        sharecart.x(1);
        assertThat(sharecart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(sharecart).isEqualTo(defaultCart);
        sharecart.y(3);
        assertThat(sharecart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(sharecart).isEqualTo(defaultCart);
        sharecart.misc(0, 14);
        assertThat(sharecart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(sharecart).isEqualTo(defaultCart);
        sharecart.misc(1, 40);
        assertThat(sharecart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(sharecart).isEqualTo(defaultCart);
        sharecart.misc(2, 6);
        assertThat(sharecart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(sharecart).isEqualTo(defaultCart);
        sharecart.misc(3, 7890);
        assertThat(sharecart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(sharecart).isEqualTo(defaultCart);
        sharecart.name("This is a custom name!");
        assertThat(sharecart).isNotEqualTo(defaultCart);

        for(int i = 0; i < Constraints.SWITCH_ITEMS_LENGTH; i++) {
            resetSharecart();
            assertThat(sharecart).isEqualTo(defaultCart);
            sharecart.switchValue(i, true);
            assertThat(sharecart).isNotEqualTo(defaultCart);
        }
    }

    private void resetSharecart() {
        sharecart = new Sharecart();
    }
}

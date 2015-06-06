package io.itch.frogcheese.sharecart;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ShareCartTest {

    private ShareCart shareCart;

    @Before
    public void setUp() throws Exception {
        shareCart = new ShareCart();
    }

    @Test
    public void testPreconditions() throws Exception {
        shareCart = new ShareCart();

        assertThat(shareCart).isNotNull();
        assertThat(shareCart.x()).isZero();
        assertThat(shareCart.y()).isZero();

        assertThat(shareCart.miscLength()).isEqualTo(Constraints.MISC_ITEMS_LENGTH);
        for (int i = 0; i < shareCart.miscLength(); i++) {
            assertThat(shareCart.misc(i)).isZero();
        }

        assertThat(shareCart.name()).isEqualTo(ShareCart.DEFAULT_NAME);

        assertThat(shareCart.switchLength()).isEqualTo(Constraints.SWITCH_ITEMS_LENGTH);
        for (int i = 0; i < shareCart.switchLength(); i++) {
            assertThat(shareCart.switchValue(i)).isFalse();
        }
    }

    @Test
    public void testConstructor_from_defaults() throws Exception {
        shareCart = ShareCart.withDefaults();

        assertThat(shareCart).isNotNull();
        assertThat(shareCart.x()).isZero();
        assertThat(shareCart.y()).isZero();

        assertThat(shareCart.miscLength()).isEqualTo(Constraints.MISC_ITEMS_LENGTH);
        for (int i = 0; i < shareCart.miscLength(); i++) {
            assertThat(shareCart.misc(i)).isZero();
        }

        assertThat(ShareCart.DEFAULT_NAME).isEqualTo("CHANGEME");
        assertThat(shareCart.name()).isEqualTo(ShareCart.DEFAULT_NAME);

        assertThat(shareCart.switchLength()).isEqualTo(Constraints.SWITCH_ITEMS_LENGTH);
        for (int i = 0; i < shareCart.switchLength(); i++) {
            assertThat(shareCart.switchValue(i)).isFalse();
        }
    }

    @Test
    public void testX_values() throws Exception {
        shareCart.x(100);
        assertThat(shareCart.x()).isEqualTo(100);

        shareCart.x(0);
        assertThat(shareCart.x()).isEqualTo(0);

        shareCart.x(Constraints.MAX_SIZE_X);
        assertThat(shareCart.x()).isEqualTo(Constraints.MAX_SIZE_X);
    }

    @Test
    public void testY_values() throws Exception {
        shareCart.y(100);
        assertThat(shareCart.y()).isEqualTo(100);

        shareCart.y(0);
        assertThat(shareCart.y()).isEqualTo(0);

        shareCart.y(Constraints.MAX_SIZE_Y);
        assertThat(shareCart.y()).isEqualTo(Constraints.MAX_SIZE_Y);
    }

    @Test
    public void testMisc_index_constraints() throws Exception {

        try {
            shareCart.misc(-1);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            shareCart.misc(Constraints.MISC_ITEMS_LENGTH);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            shareCart.misc(-1, 0);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            shareCart.misc(Constraints.MISC_ITEMS_LENGTH, 0);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        shareCart.misc(0);
        shareCart.misc(Constraints.MISC_ITEMS_LENGTH - 1);

        shareCart.misc(0, 0);
        shareCart.misc(Constraints.MISC_ITEMS_LENGTH - 1, 0);
    }

    @Test
    public void testMisc_value_set_to_correct_index() throws Exception {
        shareCart.misc(0, 20);

        assertThat(shareCart.misc(0)).isEqualTo(20);
        assertThat(shareCart.misc(1)).isZero();
        assertThat(shareCart.misc(2)).isZero();
        assertThat(shareCart.misc(3)).isZero();

        shareCart.misc(1, 44);

        assertThat(shareCart.misc(0)).isEqualTo(20);
        assertThat(shareCart.misc(1)).isEqualTo(44);
        assertThat(shareCart.misc(2)).isZero();
        assertThat(shareCart.misc(3)).isZero();

        shareCart.misc(2, 67);

        assertThat(shareCart.misc(0)).isEqualTo(20);
        assertThat(shareCart.misc(1)).isEqualTo(44);
        assertThat(shareCart.misc(2)).isEqualTo(67);
        assertThat(shareCart.misc(3)).isZero();

        shareCart.misc(3, 12);

        assertThat(shareCart.misc(0)).isEqualTo(20);
        assertThat(shareCart.misc(1)).isEqualTo(44);
        assertThat(shareCart.misc(2)).isEqualTo(67);
        assertThat(shareCart.misc(3)).isEqualTo(12);

        shareCart.misc(1, 0);
        shareCart.misc(3, 0);

        assertThat(shareCart.misc(0)).isEqualTo(20);
        assertThat(shareCart.misc(1)).isZero();
        assertThat(shareCart.misc(2)).isEqualTo(67);
        assertThat(shareCart.misc(3)).isZero();
    }

    @Test
    public void testMisc_values() throws Exception {
        shareCart.misc(0, 20);
        assertThat(shareCart.misc(0)).isEqualTo(20);

        shareCart.misc(0, 0);
        assertThat(shareCart.misc(0)).isEqualTo(0);

        shareCart.misc(0, Constraints.MAX_SIZE_MISC);
        assertThat(shareCart.misc(0)).isEqualTo(Constraints.MAX_SIZE_MISC);
    }

    @Test
    public void testName_values() throws Exception {
        shareCart.name("a name");
        assertThat(shareCart.name()).isEqualTo("a name");

        shareCart.name(null);
        assertThat(shareCart.name()).isNull();

        shareCart.name("");
        assertThat(shareCart.name()).isEmpty();

        shareCart.name("1234567");
        assertThat(shareCart.name()).isEqualTo("1234567");
    }

    @Test
    public void testSwitch_index_constraints() throws Exception {

        try {
            shareCart.switchValue(-1);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            shareCart.switchValue(Constraints.SWITCH_ITEMS_LENGTH);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            shareCart.switchValue(-1, false);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            shareCart.switchValue(Constraints.SWITCH_ITEMS_LENGTH, false);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException ignored) {

        }

        shareCart.switchValue(0);
        shareCart.switchValue(Constraints.SWITCH_ITEMS_LENGTH - 1);

        shareCart.switchValue(0, false);
        shareCart.switchValue(Constraints.SWITCH_ITEMS_LENGTH - 1, false);
    }

    @Test
    public void testSwitch_value_set_to_correct_index() throws Exception {
        shareCart.switchValue(0, true);
        assertThat(shareCart.switchValue(0)).isTrue();
        assertThat(shareCart.switchValue(1)).isFalse();
        assertThat(shareCart.switchValue(2)).isFalse();
        assertThat(shareCart.switchValue(3)).isFalse();
        assertThat(shareCart.switchValue(4)).isFalse();
        assertThat(shareCart.switchValue(5)).isFalse();
        assertThat(shareCart.switchValue(6)).isFalse();
        assertThat(shareCart.switchValue(7)).isFalse();

        shareCart.switchValue(1, true);
        assertThat(shareCart.switchValue(0)).isTrue();
        assertThat(shareCart.switchValue(1)).isTrue();
        assertThat(shareCart.switchValue(2)).isFalse();
        assertThat(shareCart.switchValue(3)).isFalse();
        assertThat(shareCart.switchValue(4)).isFalse();
        assertThat(shareCart.switchValue(5)).isFalse();
        assertThat(shareCart.switchValue(6)).isFalse();
        assertThat(shareCart.switchValue(7)).isFalse();

        shareCart.switchValue(5, true);
        assertThat(shareCart.switchValue(0)).isTrue();
        assertThat(shareCart.switchValue(1)).isTrue();
        assertThat(shareCart.switchValue(2)).isFalse();
        assertThat(shareCart.switchValue(3)).isFalse();
        assertThat(shareCart.switchValue(4)).isFalse();
        assertThat(shareCart.switchValue(5)).isTrue();
        assertThat(shareCart.switchValue(6)).isFalse();
        assertThat(shareCart.switchValue(7)).isFalse();

        shareCart.switchValue(1, false);
        assertThat(shareCart.switchValue(0)).isTrue();
        assertThat(shareCart.switchValue(1)).isFalse();
        assertThat(shareCart.switchValue(2)).isFalse();
        assertThat(shareCart.switchValue(3)).isFalse();
        assertThat(shareCart.switchValue(4)).isFalse();
        assertThat(shareCart.switchValue(5)).isTrue();
        assertThat(shareCart.switchValue(6)).isFalse();
        assertThat(shareCart.switchValue(7)).isFalse();
    }

    @Test
    public void testSwitch_values() throws Exception {
        shareCart.switchValue(0, true);
        assertThat(shareCart.switchValue(0)).isTrue();

        shareCart.switchValue(0, false);
        assertThat(shareCart.switchValue(0)).isFalse();
    }

    @Test
    public void testEquals_requires_all_parameters_to_be_equal() throws Exception {
        ShareCart defaultCart = ShareCart.withDefaults();

        assertThat(shareCart).isEqualTo(defaultCart);
        shareCart.x(1);
        assertThat(shareCart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(shareCart).isEqualTo(defaultCart);
        shareCart.y(3);
        assertThat(shareCart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(shareCart).isEqualTo(defaultCart);
        shareCart.misc(0, 14);
        assertThat(shareCart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(shareCart).isEqualTo(defaultCart);
        shareCart.misc(1, 40);
        assertThat(shareCart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(shareCart).isEqualTo(defaultCart);
        shareCart.misc(2, 6);
        assertThat(shareCart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(shareCart).isEqualTo(defaultCart);
        shareCart.misc(3, 7890);
        assertThat(shareCart).isNotEqualTo(defaultCart);

        resetSharecart();
        assertThat(shareCart).isEqualTo(defaultCart);
        shareCart.name("This is a custom name!");
        assertThat(shareCart).isNotEqualTo(defaultCart);

        for (int i = 0; i < Constraints.SWITCH_ITEMS_LENGTH; i++) {
            resetSharecart();
            assertThat(shareCart).isEqualTo(defaultCart);
            shareCart.switchValue(i, true);
            assertThat(shareCart).isNotEqualTo(defaultCart);
        }
    }

    private void resetSharecart() {
        shareCart = new ShareCart();
    }
}

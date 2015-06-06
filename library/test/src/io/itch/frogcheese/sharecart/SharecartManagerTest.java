package io.itch.frogcheese.sharecart;


import io.itch.frogcheese.sharecart._test.Utils;
import io.itch.frogcheese.sharecart.error.InvalidParameterException;
import org.junit.Test;
import org.mockito.Mockito;

import static io.itch.frogcheese.sharecart.Constraints.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Matchers.any;

public class ShareCartManagerTest extends ShareCartManagerTestBase {

    @Test
    public void testChange_params_after_load() throws Exception {
        loadSharecart();

        assertThat(manager.isLoaded()).isTrue();
        assertThat(manager.hasUnsavedChanges()).isFalse();

        manager.x(10);
        assertThat(manager.hasUnsavedChanges());

        manager.y(20);
        manager.misc(0, 1);
        manager.misc(1, 20);
        manager.misc(2, 40);
        manager.misc(3, 110);
        manager.name("A new name from a test");
        manager.switchValue(0, true);
        manager.switchValue(1, false);
        manager.switchValue(2, true);
        manager.switchValue(3, false);
        manager.switchValue(4, true);
        manager.switchValue(5, true);
        manager.switchValue(6, false);
        manager.switchValue(7, false);

        assertThat(manager.x()).isEqualTo(10);
        assertThat(manager.y()).isEqualTo(20);
        assertThat(manager.misc(0)).isEqualTo(1);
        assertThat(manager.misc(1)).isEqualTo(20);
        assertThat(manager.misc(2)).isEqualTo(40);
        assertThat(manager.misc(3)).isEqualTo(110);
        assertThat(manager.name()).isEqualTo("A new name from a test");
        assertThat(manager.switchValue(0)).isTrue();
        assertThat(manager.switchValue(1)).isFalse();
        assertThat(manager.switchValue(2)).isTrue();
        assertThat(manager.switchValue(3)).isFalse();
        assertThat(manager.switchValue(4)).isTrue();
        assertThat(manager.switchValue(5)).isTrue();
        assertThat(manager.switchValue(6)).isFalse();
        assertThat(manager.switchValue(7)).isFalse();
    }

    @Test
    public void testFailing_x_constraints_throws_exception() throws Exception {
        loadSharecart();

        try {
            manager.x(-1);
            failBecauseExceptionWasNotThrown(InvalidParameterException.class);
        } catch (InvalidParameterException e) {
            assertThat(e).hasMessage("'-1' is not a valid value for parameter 'x'");
        }

        manager.x(0);
        manager.x(MAX_SIZE_X);
        try {
            manager.x(MAX_SIZE_X + 1);
            failBecauseExceptionWasNotThrown(InvalidParameterException.class);
        } catch (InvalidParameterException e) {
            assertThat(e).hasMessage("'1024' is not a valid value for parameter 'x'");
        }
    }

    @Test
    public void testFailing_y_constraints_throws_exception() throws Exception {
        loadSharecart();

        try {
            manager.y(-1);
            failBecauseExceptionWasNotThrown(InvalidParameterException.class);
        } catch (InvalidParameterException e) {
            assertThat(e).hasMessage("'-1' is not a valid value for parameter 'y'");
        }

        manager.y(0);
        manager.y(Constraints.MAX_SIZE_Y);
        try {
            manager.y(Constraints.MAX_SIZE_Y + 1);
            failBecauseExceptionWasNotThrown(InvalidParameterException.class);
        } catch (InvalidParameterException e) {
            assertThat(e).hasMessage("'1024' is not a valid value for parameter 'y'");
        }
    }

    @Test
    public void testFailing_misc_constraints_throws_exception() throws Exception {
        loadSharecart();

        for (int i = 0; i < MISC_ITEMS_LENGTH; i++) {
            try {
                manager.misc(i, -1);
            } catch (InvalidParameterException e) {
                assertThat(e).hasMessage("'-1' is not a valid value for parameter 'misc" + i + "'");
            }

            manager.misc(i, 0);
            manager.misc(i, Constraints.MAX_SIZE_MISC);

            try {
                manager.misc(i, Constraints.MAX_SIZE_MISC + 1);
            } catch (InvalidParameterException e) {
                assertThat(e).hasMessage("'65536' is not a valid value for parameter 'misc" + i + "'");
            }
        }
    }

    @Test
    public void testFailing_misc_index_constraints_throws_exception() throws Exception {
        loadSharecart();
        try {
            manager.misc(-1);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException e) {
            assertThat(e).hasMessage("misc-1");
        }

        try {
            manager.misc(-1, 10);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException e) {
            assertThat(e).hasMessage("misc-1");
        }

        try {
            manager.misc(MISC_ITEMS_LENGTH);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException e) {
            assertThat(e).hasMessage("misc4");
        }

        try {
            manager.misc(MISC_ITEMS_LENGTH, 20);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException e) {
            assertThat(e).hasMessage("misc4");
        }
    }

    @Test
    public void testFailing_name_constraints_throws_exception() throws Exception {
        loadSharecart();

        try {
            manager.name(null);
            failBecauseExceptionWasNotThrown(InvalidParameterException.class);
        } catch (InvalidParameterException e) {
            assertThat(e).hasMessage("'null' is not a valid value for parameter 'name'");
        }

        StringBuilder sb = Utils.getStringBuilderWithLength(MAX_SIZE_NAME_LENGTH, 'a');

        manager.name(sb.toString());

        sb.append('a');

        try {
            manager.name(sb.toString());
            failBecauseExceptionWasNotThrown(InvalidParameterException.class);
        } catch (InvalidParameterException e) {
            assertThat(e).hasMessage("'" + sb.toString() + "' is not a valid value for parameter 'name'");
        }
    }

    @Test
    public void testFailing_switch_index_constraints_throws_exception() throws Exception {
        loadSharecart();
        try {
            manager.switchValue(-1);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException e) {
            assertThat(e).hasMessage("switch-1");
        }

        try {
            manager.switchValue(-1, true);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException e) {
            assertThat(e).hasMessage("switch-1");
        }

        try {
            manager.switchValue(SWITCH_ITEMS_LENGTH);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException e) {
            assertThat(e).hasMessage("switch8");
        }

        try {
            manager.switchValue(SWITCH_ITEMS_LENGTH, true);
            failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
        } catch (IndexOutOfBoundsException e) {
            assertThat(e).hasMessage("switch8");
        }
    }

    @Test
    public void testClamp_parameters_to_constraints() throws Exception {
        config = new ShareCartConfig.Builder()
                .setClampToConstraints(true)
                .build();
        ShareCartManager.initialize(config);
        manager = ShareCartManager.get();
        loadSharecart();

        manager.x(-1);
        assertThat(manager.x()).isZero();

        manager.x(MAX_SIZE_X + 1);
        assertThat(manager.x()).isEqualTo(MAX_SIZE_X);

        manager.y(-1);
        assertThat(manager.y()).isZero();

        manager.y(MAX_SIZE_Y + 1);
        assertThat(manager.y()).isEqualTo(MAX_SIZE_Y);

        for (int i = 0; i < MISC_ITEMS_LENGTH; i++) {
            manager.misc(i, -1);
            assertThat(manager.misc(i)).isZero();

            manager.misc(i, MAX_SIZE_MISC + 1);
            assertThat(manager.misc(i)).isEqualTo(MAX_SIZE_MISC);
        }

        manager.name(null);
        assertThat(manager.name()).isEqualTo("");

        StringBuilder sb = Utils.getStringBuilderWithLength(MAX_SIZE_NAME_LENGTH, 'a');
        String nameWithinConstraints = sb.toString();
        String nameOutsideConstraints = sb.append('a').toString();

        manager.name(nameOutsideConstraints);
        assertThat(manager.name()).isEqualTo(nameWithinConstraints);
    }

    @Test
    public void testFail_index_constraints_with_clamp_to_constraints() throws Exception {
        config = new ShareCartConfig.Builder()
                .setClampToConstraints(true)
                .build();
        ShareCartManager.initialize(config);
        manager = ShareCartManager.get();

        testFailing_misc_index_constraints_throws_exception();
        testFailing_switch_index_constraints_throws_exception();
    }

    @Test
    public void testSave() throws Exception {
        loadSharecart();

        manager.x(10);
        assertThat(manager.hasUnsavedChanges()).isTrue();

        manager.save();

        Mockito.verify(mockWriter).write(any(ShareCart.class));

        assertThat(manager.hasUnsavedChanges()).isFalse();

        assertThat(manager.x()).isEqualTo(10);
    }
}

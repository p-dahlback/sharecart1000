package io.itch.frogcheese.sharecart.error;


import org.junit.Test;

import java.util.IllegalFormatConversionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ShareCartFormatExceptionTest {

    @Test
    public void testConstruct_with_params() throws Exception {
        ShareCartFormatException exception = new ShareCartFormatException("A test %s string %d with format %.2f",
                "this is a string", 24, 1f);

        assertThat(exception).hasMessage("A test this is a string string 24 with format 1.00");
    }

    @Test
    public void testConstruct_with_params_illegal_format() throws Exception {
        try {
            //noinspection ThrowableInstanceNeverThrown
            new ShareCartFormatException("A test %s string %d with format %.2f",
                    24, "this is a string", 1);
            failBecauseExceptionWasNotThrown(IllegalFormatConversionException.class);
        } catch (IllegalFormatConversionException ignored) {

        }
    }

    @Test
    public void testConstruct_without_params() throws Exception {
        ShareCartFormatException exception = new ShareCartFormatException("A test string");

        assertThat(exception).hasMessage("A test string");
    }

    @Test
    public void testConstruct_with_null_throws_exception() throws Exception {
        try {
            //noinspection ThrowableInstanceNeverThrown
            new ShareCartFormatException(null);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException ignored) {

        }
    }

    @Test
    public void testExtends_sharecart_exception() throws Exception {
        ShareCartFormatException exception = new ShareCartFormatException("test");

        assertThat(exception).isInstanceOf(ShareCartException.class);
    }

}

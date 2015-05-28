package io.itch.frogcheese.sharecart.error;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InvalidParameterExceptionTest {

    @Test
    public void testConstruct_with_int_parameter() throws Exception {
        InvalidParameterException exception = new InvalidParameterException("testValInt", 14);

        assertThat(exception).hasMessage("'14' is not a valid value for parameter 'testValInt'");
    }

    @Test
    public void testConstruct_with_string_parameter() throws Exception {
        InvalidParameterException exception = new InvalidParameterException("testValString", "a string!!");

        assertThat(exception).hasMessage("'a string!!' is not a valid value for parameter 'testValString'");
    }

    @Test
    public void testExtends_sharecart_exception() throws Exception {
        InvalidParameterException exception = new InvalidParameterException("test", 0);

        assertThat(exception).isInstanceOf(SharecartException.class);
    }
}

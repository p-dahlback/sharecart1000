package io.itch.frogcheese.sharecart.error;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParameterNotAccessibleExceptionTest {

    @Test
    public void testConstructor() throws Exception {
        ParameterNotAccessibleException exception = new ParameterNotAccessibleException("aTestParam");

        assertThat(exception).hasMessage("Can't access parameter 'aTestParam' without valid sharecart file.");
    }

    @Test
    public void testExtends_sharecart_exception() throws Exception {
        ParameterNotAccessibleException exception = new ParameterNotAccessibleException("test");

        assertThat(exception).isInstanceOf(SharecartException.class);
    }

}

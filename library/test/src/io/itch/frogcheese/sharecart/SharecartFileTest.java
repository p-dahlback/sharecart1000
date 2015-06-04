package io.itch.frogcheese.sharecart;


import io.itch.frogcheese.sharecart._test.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ShareCartFileTest {

    private File file;

    @Before
    public void setUp() throws Exception {
        file = new File(Constants.TEST_RESOURCES_PATH, "dat/o_o.ini");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testPreconditions() throws Exception {

    }

    @Test
    public void testCreate_from_file() throws Exception {
        ShareCartFile model = ShareCartFile.fromFile(file);

        assertThat(model).isNotNull();
        assertThat(model.getFile()).isSameAs(file);
        assertThat(model.isAutoCreated()).isFalse();
    }

    @Test
    public void testCreate_from_file_rejects_null() throws Exception {
        try {
            ShareCartFile.fromFile(null);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage("Cannot pass null as a file.");
        }
    }

    @Test
    public void testCreate_from_file_autocreated() throws Exception {
        ShareCartFile model = ShareCartFile.fromAutoCreatedFile(file);

        assertThat(model).isNotNull();
        assertThat(model.getFile()).isSameAs(file);
        assertThat(model.isAutoCreated()).isTrue();
    }

    @Test
    public void testCreate_from_file_autocreated_rejects_null() throws Exception {
        try {
            ShareCartFile.fromAutoCreatedFile(null);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage("Cannot pass null as a file.");
        }
    }

    @Test
    public void testSetAutoCreated() throws Exception {
        ShareCartFile model = ShareCartFile.fromFile(file);

        model.setIsAutoCreated(true);

        assertThat(model.isAutoCreated()).isTrue();

        model.setIsAutoCreated(false);

        assertThat(model.isAutoCreated()).isFalse();
    }
}

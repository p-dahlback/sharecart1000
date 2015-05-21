package io.itch.frogcheese.sharecart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class SharecartFileUtilsTest {

    private String runningDirectory;

    @Before
    public void setUp() throws Exception {
        runningDirectory = System.getProperty("user.dir");
        System.clearProperty("application.dir");
        SharecartFileUtils.APP_DIR = null;
        SharecartFileUtils.RUNNING_DIR = null;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPreconditions() throws Exception {
        assertThat(runningDirectory).isNotNull();
    }

    @Test
    public void testRunningDirectory_set_from_system_properties() throws Exception {
        assertThat(SharecartFileUtils.getRunningDirectory()).isEqualTo(runningDirectory);
    }

    @Test
    public void testApplicationPath_same_as_running_directory() throws Exception {
        assertThat(SharecartFileUtils.getApplicationPath()).isEqualTo(runningDirectory);
    }

    @Test
    public void testApplicationPath_set_from_system_properties() throws Exception {
        System.setProperty("application.dir", "a/path/to/the/application");

        assertThat(SharecartFileUtils.getApplicationPath()).isEqualTo("a/path/to/the/application");
    }

    @Test
    public void testFile_from_running_path() throws Exception {
        assertThat(SharecartFileUtils.getFileFromRunningDirectory("file"))
                .isEqualTo(new File(runningDirectory + "/file"));

        assertThat(SharecartFileUtils.getFileFromRunningDirectory("file/with/a/path"))
                .isEqualTo(new File(runningDirectory + "/file/with/a/path"));
    }

    @Test
    public void testDirectories_above_running_path() throws Exception {
        assertThat(SharecartFileUtils.getFileAboveRunningDirectory(2, "file"))
                .isEqualTo(new File(runningDirectory + "/../../file"));

        assertThat(SharecartFileUtils.getFileAboveRunningDirectory(3, "file"))
                .isEqualTo(new File(runningDirectory + "/../../../file"));
    }

    @Test
    public void testDirectories_above_directory() throws Exception {
        assertThat(SharecartFileUtils.getFileAboveDirectory(2, "a/dir/", "file"))
                .isEqualTo(new File("a/dir/../../file"));

        assertThat(SharecartFileUtils.getFileAboveDirectory(1, "a/dir/", "file"))
                .isEqualTo(new File("a/dir/../file"));
    }
}

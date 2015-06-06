package io.itch.frogcheese.sharecart;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class ShareCartFileUtilsTest {

    private String runningDirectory;

    @Before
    public void setUp() throws Exception {
        runningDirectory = System.getProperty("user.dir");
        System.clearProperty("application.dir");
        ShareCartFileUtils.APP_DIR = null;
        ShareCartFileUtils.RUNNING_DIR = null;
    }

    @Test
    public void testPreconditions() throws Exception {
        assertThat(runningDirectory).isNotNull();
    }

    @Test
    public void testRunningDirectory_set_from_system_properties() throws Exception {
        assertThat(ShareCartFileUtils.getRunningDirectory()).isEqualTo(runningDirectory);
    }

    @Test
    public void testApplicationPath_same_as_running_directory() throws Exception {
        assertThat(ShareCartFileUtils.getApplicationPath()).isEqualTo(runningDirectory);
    }

    @Test
    public void testApplicationPath_set_from_system_properties() throws Exception {
        System.setProperty("application.dir", "a/path/to/the/application");

        assertThat(ShareCartFileUtils.getApplicationPath()).isEqualTo("a/path/to/the/application");
    }

    @Test
    public void testFile_from_running_path() throws Exception {
        assertThat(ShareCartFileUtils.getFileFromRunningDirectory("file"))
                .isEqualTo(new File(runningDirectory + "/file"));

        assertThat(ShareCartFileUtils.getFileFromRunningDirectory("file/with/a/path"))
                .isEqualTo(new File(runningDirectory + "/file/with/a/path"));
    }

    @Test
    public void testDirectories_above_running_path() throws Exception {
        assertThat(ShareCartFileUtils.getFileAboveRunningDirectory(2, "file"))
                .isEqualTo(new File(runningDirectory + "/../../file"));

        assertThat(ShareCartFileUtils.getFileAboveRunningDirectory(3, "file"))
                .isEqualTo(new File(runningDirectory + "/../../../file"));
    }

    @Test
    public void testDirectories_above_directory() throws Exception {
        assertThat(ShareCartFileUtils.getFileAboveDirectory(2, "a/dir/", "file"))
                .isEqualTo(new File("a/dir/../../file"));

        assertThat(ShareCartFileUtils.getFileAboveDirectory(1, "a/dir/", "file"))
                .isEqualTo(new File("a/dir/../file"));
    }
}

package io.itch.frogcheese.sharecart;


import io.itch.frogcheese.sharecart._test.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.*;

public class ShareCartFileInterfaceTest {

    private ShareCartFile validShareCartFile;
    private ShareCartFile invalidShareCartFile;

    @Before
    public void setUp() throws Exception {
        File validFile = new File(Constants.TEST_INI_FILE_PATH);
        File invalidFile = new File(Constants.TEST_RESOURCES_PATH, "invalidfile");

        validShareCartFile = ShareCartFile.fromFile(validFile);
        invalidShareCartFile = ShareCartFile.fromFile(invalidFile);
    }

    @After
    public void tearDown() throws Exception {
        ShareCartFileInterface.inject(null);

        // Clean out any files that were created
        FileUtils.cleanDirectory(new File(Constants.TEST_RESOURCES_PATH, "games"));
        File subFolderDirectory = new File(Constants.TEST_EMPTY_SUBFOLDER_PATH);
        if (!subFolderDirectory.mkdirs()) {
            fail("Failed to recreate directory after test: "
                    + Constants.TEST_EMPTY_SUBFOLDER_PATH);
        }
    }

    @Test
    public void testPreconditions() throws Exception {
        assertThat(validShareCartFile).isNotNull();
        assertThat(validShareCartFile.getFile()).exists();
        assertThat(validShareCartFile.getFile()).isFile();

        assertThat(invalidShareCartFile).isNotNull();
        assertThat(invalidShareCartFile.getFile()).doesNotExist();
    }

    @Test
    public void testGet_default_instance() throws Exception {
        ShareCartFileInterface instance = ShareCartFileInterface.get();

        assertThat(instance).isNotNull();
    }

    @Test
    public void testInject() throws Exception {
        ShareCartFileInterface impl = Mockito.mock(ShareCartFileInterface.class);

        ShareCartFileInterface.inject(impl);

        assertThat(ShareCartFileInterface.get()).isSameAs(impl);
    }

    @Test
    public void testInjectResets_with_null() throws Exception {
        ShareCartFileInterface impl = Mockito.mock(ShareCartFileInterface.class);
        ShareCartFileInterface.inject(impl);
        ShareCartFileInterface.inject(null);

        ShareCartFileInterface instance = ShareCartFileInterface.get();

        assertThat(instance).isNotNull();
        assertThat(instance).isNotSameAs(impl);
    }

    @Test
    public void testNew_sharecart_reader() throws Exception {
        ShareCartFileInterface instance = ShareCartFileInterface.get();

        ShareCartFileReader reader = instance.getNewSharecartFileReader(validShareCartFile);

        assertThat(reader).isNotNull();
    }

    @Test
    public void testNew_sharecart_reader_filenotfound_for_invalid_file() throws Exception {
        ShareCartFileInterface instance = ShareCartFileInterface.get();

        try {
            instance.getNewSharecartFileReader(invalidShareCartFile);
            failBecauseExceptionWasNotThrown(FileNotFoundException.class);
        } catch (FileNotFoundException ignored) {

        }
    }

    @Test
    public void testNew_sharecart_writer_filenotfound_for_invalid_file() throws Exception {
        ShareCartFileInterface instance = ShareCartFileInterface.get();

        try {
            instance.getNewSharecartFileWriter(invalidShareCartFile);
            failBecauseExceptionWasNotThrown(FileNotFoundException.class);
        } catch (FileNotFoundException ignored) {

        }
    }

    @Test
    public void testFind_ini_file_from_same_directory() throws Exception {
        ShareCartFileInterface instance = ShareCartFileInterface.get();

        ShareCartFile file = instance.findIniFile(0, Constants.TEST_RESOURCES_PATH);
        assertThat(file).isNotNull();
        assertThat(file.isAutoCreated()).isFalse();
        assertThat(normalizePath(file)).isEqualTo(normalizePath(validShareCartFile));
    }

    @Test
    public void testFind_ini_file_from_subdirectory() throws Exception {
        ShareCartFileInterface instance = ShareCartFileInterface.get();

        ShareCartFile file = instance.findIniFile(3, Constants.TEST_EMPTY_SUBFOLDER_PATH);
        assertThat(file).isNotNull();
        assertThat(file.isAutoCreated()).isFalse();
        assertThat(normalizePath(file)).isEqualTo(normalizePath(validShareCartFile));
    }

    @Test
    public void testFind_ini_file_fails_when_not_enough_directories_checked() throws Exception {
        ShareCartFileInterface instance = ShareCartFileInterface.get();

        ShareCartFile file = instance.findIniFile(2, Constants.TEST_EMPTY_SUBFOLDER_PATH);
        assertThat(file).isNull();
    }

    /**
     * Fail because the structure needs to be 'dat/o_o.ini'.
     * If we don't find the 'dat' folder, the structure is invalid.
     *
     * @throws Exception
     */
    @Test
    public void testFind_ini_file_fails_from_dat_folder() throws Exception {
        ShareCartFileInterface instance = ShareCartFileInterface.get();

        ShareCartFile file = instance.findIniFile(0, Constants.TEST_DAT_FOLDER_PATH);
        assertThat(file).isNull();
    }

    @Test
    public void testFind_or_create_finds_file_from_same_directory() throws Exception {
        ShareCartFileInterface instance = ShareCartFileInterface.get();

        ShareCartFile file = instance.findOrCreateIniFile(0, Constants.TEST_RESOURCES_PATH);
        assertThat(file).isNotNull();
        assertThat(file.isAutoCreated()).isFalse();
        assertThat(normalizePath(file)).isEqualTo(normalizePath(validShareCartFile));
    }

    @Test
    public void testFind_or_create_finds_file_from_subdirectory() throws Exception {
        ShareCartFileInterface instance = ShareCartFileInterface.get();

        ShareCartFile file = instance.findOrCreateIniFile(3, Constants.TEST_EMPTY_SUBFOLDER_PATH);
        assertThat(file).isNotNull();
        assertThat(file.isAutoCreated()).isFalse();
        assertThat(normalizePath(file)).isEqualTo(normalizePath(validShareCartFile));
    }

    @Test
    public void testFind_or_create_creates_new_structure_one_level_above_subdirectory() throws Exception {
        ShareCartFileInterface instance = ShareCartFileInterface.get();

        ShareCartFile file = instance.findOrCreateIniFile(2, Constants.TEST_EMPTY_SUBFOLDER_PATH);
        assertThat(file).isNotNull();
        assertThat(file.isAutoCreated()).isTrue();
        assertThat(file.getFile()).exists();
        assertThat(file.getFile()).isFile();
        assertThat(normalizePath(file)).isEqualTo(normalizePath(new File(Constants.TEST_SUBFOLDER_INI_FILE_PATH)));
    }

    private static String normalizePath(File file) {
        return FilenameUtils.normalize(file.getAbsolutePath());
    }

    private static String normalizePath(ShareCartFile file) {
        return normalizePath(file.getFile());
    }

}

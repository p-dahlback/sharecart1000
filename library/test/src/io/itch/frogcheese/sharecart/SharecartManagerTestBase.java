package io.itch.frogcheese.sharecart;


import io.itch.frogcheese.sharecart._test.Constants;
import io.itch.frogcheese.sharecart.error.ParameterNotAccessibleException;
import io.itch.frogcheese.sharecart.error.ParameterNotAccessibleExceptionTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@Ignore("Base class containing some setup tests and logic")
public abstract class SharecartManagerTestBase {

    protected static final String APPLICATION_PATH = Constants.TEST_RESOURCES_PATH + "games/subfolder/subsubfolder";

    protected SharecartFileReader mockReader;
    protected SharecartFileWriter mockWriter;
    protected SharecartFileInterface mockFileInterface;

    protected SharecartFile sharecartFile;

    protected SharecartManager manager;

    protected SharecartConfig config;

    @Before
    public void setUp() throws Exception {
        mockReader = mock(SharecartFileReader.class);
        mockWriter = mock(SharecartFileWriter.class);
        mockFileInterface = mock(SharecartFileInterface.class);

        when(mockFileInterface.getNewSharecartFileReader(any(SharecartFile.class))).thenReturn(mockReader);
        when(mockFileInterface.getNewSharecartFileWriter(any(SharecartFile.class))).thenReturn(mockWriter);

        SharecartFileInterface.inject(mockFileInterface);

        sharecartFile = SharecartFile.fromFile(new File(Constants.TEST_RESOURCES_PATH, "dat/o_o.ini"));
        config = new SharecartConfig.Builder()
                .setDirectoryLevelsToCheck(3)
                .setAutoCreateFile(false)
                .setClampToConstraints(false)
                .setApplicationPath(APPLICATION_PATH)
                .build();
        SharecartManager.initialize(config);
        manager = SharecartManager.get();
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(mockFileInterface, mockReader, mockWriter);
    }

    @Test
    public void testPreconditions() throws Exception {
        assertThat(config).isNotNull();
        assertThat(manager).isNotNull();
        assertThat(sharecartFile).isNotNull();
        assertThat(sharecartFile.getFile()).isFile();

        assertThat(mockReader).isNotNull();
        assertThat(mockWriter).isNotNull();

        MockUtil mockUtil = new MockUtil();
        assertThat(mockUtil.isMock(mockReader)).isTrue();
        assertThat(mockUtil.isMock(mockWriter)).isTrue();
        assertThat(mockUtil.isMock(mockFileInterface)).isTrue();

        SharecartFileInterface fileInterface = SharecartFileInterface.get();
        assertThat(fileInterface.getNewSharecartFileReader(null)).isSameAs(mockReader);
        assertThat(fileInterface.getNewSharecartFileWriter(null)).isSameAs(mockWriter);

        assertThat(manager.isValidSharecartFile()).isFalse();
        assertThat(manager.isLoaded()).isFalse();
        assertThat(manager.hasUnsavedChanges()).isFalse();
    }

    @Test
    public void testConfig_propagates() throws Exception {
        assertThat(manager.getConfig()).isSameAs(config);
    }

    @Test
    public void testFind_valid_file_above_app_directory_failure() throws Exception {
        mockFileSearchResult(null);
        boolean valid = manager.validateSharecartFile();
        assertThat(valid).isFalse();
        assertThat(manager.isValidSharecartFile()).isFalse();
        assertThat(manager.isLoaded()).isFalse();
        assertThat(manager.hasUnsavedChanges()).isFalse();

        verify(mockFileInterface).findIniFile(config.getDirectoryLevelsToCheck(),
                config.getApplicationPath());
        verify(mockFileInterface, never()).findOrCreateIniFile(anyInt(), anyString());
    }

    @Test
    public void testFind_valid_file_above_app_directory_success() throws Exception {
        mockFileSearchResult(sharecartFile);
        boolean valid = manager.validateSharecartFile();
        assertThat(valid).isTrue();
        assertThat(manager.isValidSharecartFile()).isTrue();
        assertThat(manager.isLoaded()).isFalse();
        assertThat(manager.hasUnsavedChanges()).isFalse();

        verify(mockFileInterface).findIniFile(config.getDirectoryLevelsToCheck(),
                config.getApplicationPath());
        verify(mockFileInterface, never()).findOrCreateIniFile(anyInt(), anyString());
    }

    @Test
    public void testAuto_create_new_sharecart_file() throws Exception {
        sharecartFile.setIsAutoCreated(true);
        mockFileSearchResult(sharecartFile);
        config = new SharecartConfig.Builder()
                .setDirectoryLevelsToCheck(3)
                .setAutoCreateFile(true)
                .setApplicationPath(APPLICATION_PATH)
                .build();
        SharecartManager.initialize(config);
        manager = SharecartManager.get();

        boolean valid = manager.validateSharecartFile();
        assertThat(valid).isTrue();
        assertThat(manager.isValidSharecartFile()).isTrue();
        assertThat(manager.isLoaded()).isFalse();

        verify(mockFileInterface).findOrCreateIniFile(config.getDirectoryLevelsToCheck(),
                config.getApplicationPath());
        verify(mockFileInterface, never()).findIniFile(anyInt(), anyString());

        boolean loaded = manager.load();
        assertThat(loaded).isTrue();
        assertThat(manager.isValidSharecartFile()).isTrue();
        assertThat(manager.isLoaded()).isTrue();

        assertThat(manager.hasUnsavedChanges()).isFalse();

        verify(mockWriter).write(any(Sharecart.class));
        verifyNoMoreInteractions(mockReader);
    }

    @Test
    public void testFind_or_create_existing_sharecart_file() throws Exception {
        sharecartFile.setIsAutoCreated(false);
        mockFileSearchResult(sharecartFile);
        config = new SharecartConfig.Builder()
                .setDirectoryLevelsToCheck(3)
                .setAutoCreateFile(true)
                .setApplicationPath(APPLICATION_PATH)
                .build();
        SharecartManager.initialize(config);
        manager = SharecartManager.get();

        boolean valid = manager.validateSharecartFile();
        assertThat(valid).isTrue();
        assertThat(manager.isValidSharecartFile()).isTrue();
        assertThat(manager.isLoaded()).isFalse();

        verify(mockFileInterface).findOrCreateIniFile(config.getDirectoryLevelsToCheck(),
                config.getApplicationPath());
        verify(mockFileInterface, never()).findIniFile(anyInt(), anyString());

        boolean loaded = manager.load();
        assertThat(loaded).isTrue();
        assertThat(manager.isValidSharecartFile()).isTrue();
        assertThat(manager.isLoaded()).isTrue();

        assertThat(manager.hasUnsavedChanges()).isFalse();

        verify(mockReader).read();
        verifyNoMoreInteractions(mockWriter);
    }

    @Test
    public void testFailure_on_parameter_access_before_load() throws Exception {
        try {
            manager.x();
            failBecauseExceptionWasNotThrown(ParameterNotAccessibleException.class);
        } catch (ParameterNotAccessibleException ignored) {
        }

        try {
            manager.x(2);
            failBecauseExceptionWasNotThrown(ParameterNotAccessibleException.class);
        } catch (ParameterNotAccessibleException ignored) {

        }

        try {
            manager.y();
            failBecauseExceptionWasNotThrown(ParameterNotAccessibleException.class);
        } catch (ParameterNotAccessibleException ignored) {
        }

        try {
            manager.y(2);
            failBecauseExceptionWasNotThrown(ParameterNotAccessibleException.class);
        } catch (ParameterNotAccessibleException ignored) {

        }

        try {
            manager.misc(0);
            failBecauseExceptionWasNotThrown(ParameterNotAccessibleException.class);
        } catch (ParameterNotAccessibleException ignored) {
        }

        try {
            manager.misc(1, 2);
            failBecauseExceptionWasNotThrown(ParameterNotAccessibleException.class);
        } catch (ParameterNotAccessibleException ignored) {

        }

        try {
            manager.name();
            failBecauseExceptionWasNotThrown(ParameterNotAccessibleException.class);
        } catch (ParameterNotAccessibleException ignored) {
        }

        try {
            manager.name("a name");
            failBecauseExceptionWasNotThrown(ParameterNotAccessibleException.class);
        } catch (ParameterNotAccessibleException ignored) {

        }

        try {
            manager.switchValue(0);
            failBecauseExceptionWasNotThrown(ParameterNotAccessibleException.class);
        } catch (ParameterNotAccessibleException ignored) {
        }

        try {
            manager.switchValue(0, true);
            failBecauseExceptionWasNotThrown(ParameterNotAccessibleException.class);
        } catch (ParameterNotAccessibleException ignored) {

        }
    }

    @Test
    public void testFailure_on_save_before_load() throws Exception {
        try {
            manager.save();
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        } catch (IllegalStateException ignored) {

        }
    }

    protected void loadSharecart() {
        mockFileSearchResult(sharecartFile);
        when(mockReader.read()).thenReturn(Sharecart.withDefaults());
        manager.validateSharecartFile();
        manager.load();

        assertThat(manager.isValidSharecartFile()).isTrue();
        assertThat(manager.isLoaded()).isTrue();
    }

    protected void mockFileSearchResult(SharecartFile file) {
        when(mockFileInterface.findIniFile(anyInt(), anyString())).thenReturn(file);
        when(mockFileInterface.findOrCreateIniFile(anyInt(), anyString())).thenReturn(file);
    }
}

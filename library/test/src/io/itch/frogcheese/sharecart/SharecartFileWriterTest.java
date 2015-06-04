package io.itch.frogcheese.sharecart;

import io.itch.frogcheese.sharecart._test.Constants;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Locale;

import static io.itch.frogcheese.sharecart.ShareCartFileConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ShareCartFileWriterTest {

    private static final String TEMP_FILE_NAME = "sharecart_writer_test";
    private static final int EXPECTED_LINE_COUNT = 16;

    private static final int X_VALUE = 44;
    private static final int Y_VALUE = 56;
    private static final int MISC_VALUES[] = new int[]{
            1000, 1, 20121, 145
    };
    private static final String NAME_VALUE = "Rob Robertson";
    private static final boolean SWITCH_VALUES[] = new boolean[]{
            true, false, true, true, false, true, false, false
    };

    private File tempFile;
    private File invalidFile;
    private ShareCartFileWriter writer;
    private BufferedReader fileReader;

    // Carts
    private ShareCart defaultCart;
    private ShareCart miscCart;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile(TEMP_FILE_NAME, null);
        invalidFile = new File(Constants.TEST_RESOURCES_PATH, "invalidFile");
        writer = new ShareCartFileWriter(tempFile);

        defaultCart = ShareCart.withDefaults();

        miscCart = new ShareCart();
        miscCart.x(X_VALUE);
        miscCart.y(Y_VALUE);
        for (int i = 0; i < MISC_VALUES.length; i++) {
            miscCart.misc(i, MISC_VALUES[i]);
        }

        miscCart.name(NAME_VALUE);

        for (int i = 0; i < SWITCH_VALUES.length; i++) {
            miscCart.switchValue(i, SWITCH_VALUES[i]);
        }
    }

    @After
    public void tearDown() throws Exception {
        //noinspection ResultOfMethodCallIgnored
        tempFile.delete();
        if (fileReader != null) {
            fileReader.close();
            fileReader = null;
        }
    }

    @Test
    public void testPreconditions() throws Exception {
        assertThat(tempFile).isFile();
        assertThat(writer).isNotNull();

        assertThat(defaultCart).isEqualTo(ShareCart.withDefaults());
        assertThat(miscCart.x()).isEqualTo(X_VALUE);
        assertThat(miscCart.y()).isEqualTo(Y_VALUE);

        for (int i = 0; i < MISC_VALUES.length; i++) {
            assertThat(miscCart.misc(i)).isEqualTo(MISC_VALUES[i]);
        }

        assertThat(miscCart.name()).isEqualTo(NAME_VALUE);

        for (int i = 0; i < SWITCH_VALUES.length; i++) {
            assertThat(miscCart.switchValue(i)).isEqualTo(SWITCH_VALUES[i]);
        }
    }

    @Test
    public void testConstruct_rejects_null() throws Exception {
        try {
            new ShareCartFileWriter(null);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException ignored) {

        }
    }

    @Test
    public void testConstruct_with_invalid_file_throws_exception() throws Exception {
        try {
            new ShareCartFileWriter(invalidFile);
            failBecauseExceptionWasNotThrown(FileNotFoundException.class);
        } catch (FileNotFoundException ignored) {

        }
    }

    @Test
    public void testWrite_correct_line_format() throws Exception {
        writer.write(defaultCart);
        writer.close();

        String fileContents = readEntireFile();
        int lineCount = StringUtils.countMatches(fileContents, "\r\n");

        int index = 0;
        while ((index = fileContents.indexOf('\n', index)) != -1) {
            assertThat(fileContents.charAt(index - 1)).isEqualTo('\r');
            index++;
        }

        assertThat(lineCount).isEqualTo(EXPECTED_LINE_COUNT);
    }

    @Test
    public void testWrite_default_values() throws Exception {
        writer.write(defaultCart);
        writer.close();

        fileReader = newFileReader();
        assertNextLineEquals(TITLE);
        assertNextLineEquals(PARAMETER_X, "0");
        assertNextLineEquals(PARAMETER_Y, "0");

        for (String miscParameter : PARAMETER_MISC) {
            assertNextLineEquals(miscParameter, "0");
        }

        assertNextLineEquals(PARAMETER_NAME, ShareCart.DEFAULT_NAME);

        for (String switchParameter : PARAMETER_SWITCH) {
            assertNextLineEquals(switchParameter, "FALSE");
        }
    }

    @Test
    public void testWrite_misc_values() throws Exception {
        writer.write(miscCart);
        writer.close();

        fileReader = newFileReader();
        assertNextLineEquals(TITLE);
        assertNextLineEquals(PARAMETER_X, X_VALUE);
        assertNextLineEquals(PARAMETER_Y, Y_VALUE);

        for (int i = 0; i < MISC_VALUES.length; i++) {
            assertNextLineEquals(PARAMETER_MISC[i], MISC_VALUES[i]);
        }

        assertNextLineEquals(PARAMETER_NAME, NAME_VALUE);

        for (int i = 0; i < SWITCH_VALUES.length; i++) {
            assertNextLineEquals(PARAMETER_SWITCH[i], SWITCH_VALUES[i]);
        }
    }

    @Test
    public void testWritten_file_is_readable() throws Exception {
        writer.write(miscCart);
        writer.close();

        ShareCartFileReader reader = new ShareCartFileReader(tempFile);
        reader.setIsStrict(true);
        ShareCart cart = reader.read();
        reader.close();

        assertThat(cart).isEqualTo(miscCart);
    }

    private void assertNextLineEquals(String value) throws Exception {
        assertThat(fileReader.readLine()).isEqualTo(value);
    }

    private void assertNextLineEquals(String parameter, String value) throws Exception {
        assertNextLineEquals(parameter + DELIMITER_VALUE + value);
    }

    private void assertNextLineEquals(String parameter, Boolean value) throws Exception {
        assertNextLineEquals(parameter, value.toString().toUpperCase(Locale.US));
    }


    private void assertNextLineEquals(String parameter, Object value) throws Exception {
        assertNextLineEquals(parameter, value.toString());
    }


    private BufferedReader newFileReader() throws Exception {
        try {
            FileReader reader = new FileReader(tempFile);
            return new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            fail("Could not open temp file", e);
            throw (e);
        }
    }

    private String readEntireFile() throws Exception {
        fileReader = newFileReader();
        char[] buffer = new char[512];
        StringBuilder sb = new StringBuilder();
        int read;
        while ((read = fileReader.read(buffer)) != -1) {
            sb.append(buffer, 0, read);
        }
        return sb.toString();
    }
}

package io.itch.frogcheese.sharecart;

import io.itch.frogcheese.sharecart._test.Constants;
import io.itch.frogcheese.sharecart.error.SharecartFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class SharecartFileReaderTest {

    private static final String TEST_RESOURCES_PATH = Constants.TEST_RESOURCES_PATH;

    private File correctFile;
    private File missingParameterFile;
    private File constraintFailureFile;
    private File invalidParamDefinitionFile;
    private File invalidParamNameFile;
    private File emptyFile;
    private File partialFile;
    private File invalidFile;
    private SharecartFileReader reader;

    @Before
    public void setUp() throws Exception {
        correctFile = new File(TEST_RESOURCES_PATH, "sharecart_correct.ini");
        missingParameterFile = new File(TEST_RESOURCES_PATH, "sharecart_missing_parameter.ini");
        constraintFailureFile = new File(TEST_RESOURCES_PATH, "sharecart_constraint_failure.ini");
        invalidParamDefinitionFile = new File(TEST_RESOURCES_PATH, "sharecart_invalid_parameter_definition.ini");
        invalidParamNameFile = new File(TEST_RESOURCES_PATH, "sharecart_invalid_parameter_name.ini");
        emptyFile = new File(TEST_RESOURCES_PATH, "sharecart_empty.ini");
        partialFile = new File(TEST_RESOURCES_PATH, "sharecart_partial.ini");
        invalidFile = new File(TEST_RESOURCES_PATH, "invalidFile");
    }

    @After
    public void tearDown() throws Exception {
        if (reader != null)
            reader.close();
        reader = null;
    }

    @Test
    public void testPreconditions() throws Exception {
        assertThat(correctFile).isFile();
        assertThat(missingParameterFile).isFile();
        assertThat(constraintFailureFile).isFile();
        assertThat(invalidParamDefinitionFile).isFile();
        assertThat(invalidParamNameFile).isFile();
    }

    @Test
    public void testConstruct_rejects_null() throws Exception {
        try {
            new SharecartFileReader(null);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException ignored) {

        }
    }

    @Test
    public void testConstruct_with_invalid_file_throws_exception() throws Exception {
        try {
            new SharecartFileReader(invalidFile);
            failBecauseExceptionWasNotThrown(FileNotFoundException.class);
        } catch (FileNotFoundException ignored) {

        }
    }

    @Test
    public void testReader_initial_state() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(correctFile);
        assertThat(reader.isStrict()).isFalse();
    }

    @Test
    public void testRead_correct_file() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(correctFile);
        reader.setIsStrict(false);

        Sharecart sharecart = reader.read();
        assertCorrectFileMatchesParameters(sharecart);
    }

    @Test
    public void testRead_correct_file_strict() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(correctFile);
        reader.setIsStrict(true);

        Sharecart sharecart = reader.read();
        assertCorrectFileMatchesParameters(sharecart);
    }

    @Test
    public void testRead_missing_param_file() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(missingParameterFile);
        reader.setIsStrict(false);

        Sharecart sharecart = reader.read();
        assertMissingParamFileMatchesParameters(sharecart);
    }

    @Test
    public void testRead_missing_param_file_strict() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(missingParameterFile);
        reader.setIsStrict(true);

        try {
            reader.read();
            failBecauseExceptionWasNotThrown(SharecartFormatException.class);
        } catch (SharecartFormatException e) {
            assertThat(e).hasMessage("File 'test/resources/sharecart_missing_parameter.ini', line 5: " +
                    "Found 'Misc3' where parameter 'Misc2' was expected");
        }
    }

    @Test
    public void testRead_constraint_failure_file() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(constraintFailureFile);
        reader.setIsStrict(false);

        Sharecart sharecart = reader.read();
        assertConstraintFailureFileMatchesParameters(sharecart);
    }

    @Test
    public void testRead_constraint_failure_file_strict() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(constraintFailureFile);
        reader.setIsStrict(true);

        try {
            reader.read();
            failBecauseExceptionWasNotThrown(SharecartFormatException.class);
        } catch (SharecartFormatException e) {
            assertThat(e).hasMessage("File 'test/resources/sharecart_constraint_failure.ini', line 2: " +
                    "The MapY value '550000' does not fulfill the constraints of the parameter");
        }
    }

    @Test
    public void testRead_invalid_param_definition_file() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(invalidParamDefinitionFile);
        reader.setIsStrict(false);

        Sharecart sharecart = reader.read();
        assertInvalidParamDefinitionFileMatchesParameters(sharecart);
    }

    @Test
    public void testRead_invalid_param_definition_file_strict() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(invalidParamDefinitionFile);
        reader.setIsStrict(true);

        try {
            reader.read();
            failBecauseExceptionWasNotThrown(SharecartFormatException.class);
        } catch (SharecartFormatException e) {
            assertThat(e).hasMessage("File 'test/resources/sharecart_invalid_parameter_definition.ini', line 3: " +
                    "'Misc038424' is not a valid parameter definition");
        }
    }

    @Test
    public void testRead_invalid_param_name_file() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(invalidParamNameFile);
        reader.setIsStrict(false);

        Sharecart sharecart = reader.read();
        assertInvalidParamNameMatchesParameters(sharecart);
    }

    @Test
    public void testRead_invalid_param_name_file_strict() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(invalidParamNameFile);
        reader.setIsStrict(true);

        try {
            reader.read();
            failBecauseExceptionWasNotThrown(SharecartFormatException.class);
        } catch (SharecartFormatException e) {
            assertThat(e).hasMessage("File 'test/resources/sharecart_invalid_parameter_name.ini', line 5: " +
                    "Found 'Misc' where parameter 'Misc2' was expected");
        }
    }

    @Test
    public void testRead_empty_file() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(emptyFile);
        reader.setIsStrict(false);

        Sharecart sharecart = reader.read();
        assertThat(sharecart).isEqualTo(Sharecart.withDefaults());
    }

    @Test
    public void testRead_empty_file_strict() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(emptyFile);
        reader.setIsStrict(true);

        try {
            reader.read();
            failBecauseExceptionWasNotThrown(SharecartFormatException.class);
        } catch (SharecartFormatException e) {
            assertThat(e).hasMessage("File 'test/resources/sharecart_empty.ini', line 0: " +
                    "Encountered end of file prematurely");
        }
    }

    @Test
    public void testRead_partial_file() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(partialFile);
        reader.setIsStrict(false);

        Sharecart sharecart = reader.read();
        assertThat(sharecart.x()).isEqualTo(100);
        assertThat(sharecart.y()).isEqualTo(1);
        assertThat(sharecart.misc(0)).isEqualTo(38424);
        assertThat(sharecart.misc(1)).isZero();
        assertThat(sharecart.misc(2)).isZero();
        assertThat(sharecart.misc(3)).isZero();

        assertThat(sharecart.name()).isEqualTo(Sharecart.DEFAULT_NAME);

        for (int i = 0; i < Constraints.SWITCH_ITEMS_LENGTH; i++) {
            assertThat(sharecart.switchValue(i)).isFalse();
        }
    }

    @Test
    public void testRead_partial_file_strict() throws Exception {
        SharecartFileReader reader = new SharecartFileReader(partialFile);
        reader.setIsStrict(true);

        try {
            reader.read();
            failBecauseExceptionWasNotThrown(SharecartFormatException.class);
        } catch (SharecartFormatException e) {
            assertThat(e).hasMessage("File 'test/resources/sharecart_partial.ini', line 4: " +
                    "Encountered end of file prematurely");
        }
    }

    private void assertCorrectFileMatchesParameters(Sharecart sharecart) {
        assertThat(sharecart.x()).isEqualTo(100);
        assertThat(sharecart.y()).isEqualTo(1);
        assertThat(sharecart.misc(0)).isEqualTo(38424);
        assertThat(sharecart.misc(1)).isEqualTo(61499);
        assertThat(sharecart.misc(2)).isEqualTo(60753);
        assertThat(sharecart.misc(3)).isEqualTo(15107);

        assertThat(sharecart.name()).isEqualTo("MY NAME IS HERE!");

        assertThat(sharecart.switchValue(0)).isFalse();
        assertThat(sharecart.switchValue(1)).isFalse();
        assertThat(sharecart.switchValue(2)).isTrue();
        assertThat(sharecart.switchValue(3)).isFalse();
        assertThat(sharecart.switchValue(4)).isFalse();
        assertThat(sharecart.switchValue(5)).isTrue();
        assertThat(sharecart.switchValue(6)).isFalse();
        assertThat(sharecart.switchValue(7)).isFalse();
    }

    private void assertMissingParamFileMatchesParameters(Sharecart sharecart) {
        assertThat(sharecart.x()).isEqualTo(100);
        assertThat(sharecart.y()).isEqualTo(1);
        assertThat(sharecart.misc(0)).isEqualTo(38424);
        assertThat(sharecart.misc(1)).isEqualTo(61499);
        assertThat(sharecart.misc(2)).isEqualTo(0);
        assertThat(sharecart.misc(3)).isEqualTo(15107);

        assertThat(sharecart.name()).isEqualTo("A name");

        assertThat(sharecart.switchValue(0)).isFalse();
        assertThat(sharecart.switchValue(1)).isFalse();
        assertThat(sharecart.switchValue(2)).isTrue();
        assertThat(sharecart.switchValue(3)).isFalse();
        assertThat(sharecart.switchValue(4)).isFalse();
        assertThat(sharecart.switchValue(5)).isTrue();
        assertThat(sharecart.switchValue(6)).isFalse();
        assertThat(sharecart.switchValue(7)).isFalse();
    }

    private void assertConstraintFailureFileMatchesParameters(Sharecart sharecart) {
        assertThat(sharecart.x()).isEqualTo(100);
        assertThat(sharecart.y()).isEqualTo(Constraints.MAX_SIZE_Y);
        assertThat(sharecart.misc(0)).isEqualTo(38424);
        assertThat(sharecart.misc(1)).isEqualTo(61499);
        assertThat(sharecart.misc(2)).isEqualTo(60753);
        assertThat(sharecart.misc(3)).isEqualTo(Constraints.MAX_SIZE_MISC);

        assertThat(sharecart.name()).isEqualTo("MY NAME IS HERE!");

        assertThat(sharecart.switchValue(0)).isFalse();
        assertThat(sharecart.switchValue(1)).isFalse();
        assertThat(sharecart.switchValue(2)).isTrue();
        assertThat(sharecart.switchValue(3)).isFalse();
        assertThat(sharecart.switchValue(4)).isFalse();
        assertThat(sharecart.switchValue(5)).isTrue();
        assertThat(sharecart.switchValue(6)).isFalse();
        assertThat(sharecart.switchValue(7)).isFalse();
    }

    private void assertInvalidParamDefinitionFileMatchesParameters(Sharecart sharecart) {
        assertThat(sharecart.x()).isEqualTo(100);
        assertThat(sharecart.y()).isEqualTo(1);
        assertThat(sharecart.misc(0)).isEqualTo(0);
        assertThat(sharecart.misc(1)).isEqualTo(61499);
        assertThat(sharecart.misc(2)).isEqualTo(60753);
        assertThat(sharecart.misc(3)).isEqualTo(15107);

        assertThat(sharecart.name()).isEqualTo("MY NAME IS HERE!");

        assertThat(sharecart.switchValue(0)).isFalse();
        assertThat(sharecart.switchValue(1)).isFalse();
        assertThat(sharecart.switchValue(2)).isTrue();
        assertThat(sharecart.switchValue(3)).isFalse();
        assertThat(sharecart.switchValue(4)).isFalse();
        assertThat(sharecart.switchValue(5)).isTrue();
        assertThat(sharecart.switchValue(6)).isFalse();
        assertThat(sharecart.switchValue(7)).isFalse();
    }

    private void assertInvalidParamNameMatchesParameters(Sharecart sharecart) {
        assertThat(sharecart.x()).isEqualTo(100);
        assertThat(sharecart.y()).isEqualTo(1);
        assertThat(sharecart.misc(0)).isEqualTo(38424);
        assertThat(sharecart.misc(1)).isEqualTo(61499);
        assertThat(sharecart.misc(2)).isEqualTo(0);
        assertThat(sharecart.misc(3)).isEqualTo(15107);

        assertThat(sharecart.name()).isEqualTo("A name");

        assertThat(sharecart.switchValue(0)).isFalse();
        assertThat(sharecart.switchValue(1)).isFalse();
        assertThat(sharecart.switchValue(2)).isTrue();
        assertThat(sharecart.switchValue(3)).isFalse();
        assertThat(sharecart.switchValue(4)).isFalse();
        assertThat(sharecart.switchValue(5)).isTrue();
        assertThat(sharecart.switchValue(6)).isFalse();
        assertThat(sharecart.switchValue(7)).isFalse();
    }
}

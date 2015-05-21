package io.itch.frogcheese.sharecart;

import io.itch.frogcheese.sharecart._test.Constants;
import io.itch.frogcheese.sharecart.error.SharecartFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class SharecartFileReaderTest {

    private static final String TEST_RESOURCES_PATH = Constants.TEST_RESOURCES_PATH;

    private File correctFile;
    private File missingParameterFile;
    private File constraintFailureFile;
    private File invalidParamDefinitionFile;
    private File invalidParamNameFile;
    private SharecartFileReader reader;

    @Before
    public void setUp() throws Exception {
        correctFile = new File(TEST_RESOURCES_PATH, "sharecart_correct.ini");
        missingParameterFile = new File(TEST_RESOURCES_PATH, "sharecart_missing_parameter.ini");
        constraintFailureFile = new File(TEST_RESOURCES_PATH, "sharecart_constraint_failure.ini");
        invalidParamDefinitionFile = new File(TEST_RESOURCES_PATH, "sharecart_invalid_parameter_definition.ini");
        invalidParamNameFile = new File(TEST_RESOURCES_PATH, "sharecart_invalid_parameter_name.ini");
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
            assertThat(e).hasMessage("Found 'Misc3' where parameter 'Misc2' was expected");
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
            assertThat(e).hasMessage("The MapY value '550000' does not fulfill the constraints of the parameter");
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
            assertThat(e).hasMessage("'Misc038424' is not a valid parameter definition");
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
            assertThat(e).hasMessage("Found 'Misc' where parameter 'Misc2' was expected");
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

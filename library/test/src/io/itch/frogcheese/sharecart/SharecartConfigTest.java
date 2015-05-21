package io.itch.frogcheese.sharecart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;


public class SharecartConfigTest {

    private SharecartConfig.Builder configBuilder;

    @Before
    public void setUp() throws Exception {
        configBuilder = new SharecartConfig.Builder();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testPreconditions() throws Exception {
        assertThat(configBuilder).isNotNull();
    }

    @Test
    public void testDefault_values() throws Exception {
        SharecartConfig config = configBuilder.build();

        assertThat(config).isNotNull();
        assertThat(config.willClampToConstraints()).isFalse();
        assertThat(config.willAutoCreateFile()).isFalse();
        assertThat(config.isStrictFileReadMode()).isFalse();
        assertThat(SharecartConfig.DEFAULT_LEVELS_TO_CHECK).isEqualTo(4);
        assertThat(config.getDirectoryLevelsToCheck()).isEqualTo(SharecartConfig.DEFAULT_LEVELS_TO_CHECK);
        assertThat(config.getApplicationPath()).isEqualTo(SharecartFileUtils.getApplicationPath());
    }

    @Test
    public void testBuilder_values_propagate_to_config() throws Exception {
        SharecartConfig config = configBuilder
                .setClampToConstraints(true)
                .setAutoCreateFile(false)
                .setApplicationPath("path/path/directory")
                .setStrictFileReadMode(true)
                .setDirectoryLevelsToCheck(11)
                .build();

        assertThat(config).isNotNull();
        assertThat(config.willClampToConstraints()).isTrue();
        assertThat(config.willAutoCreateFile()).isFalse();
        assertThat(config.getApplicationPath()).isEqualTo("path/path/directory");
        assertThat(config.isStrictFileReadMode()).isTrue();
        assertThat(config.getDirectoryLevelsToCheck()).isEqualTo(11);
    }

    @Test
    public void testBuilder_default_values_for_unset_properties() throws Exception {
        SharecartConfig config = configBuilder
                .setApplicationPath("path/path/directory")
                .setStrictFileReadMode(true)
                .build();

        assertThat(config).isNotNull();
        assertThat(config.willClampToConstraints()).isFalse();
        assertThat(config.willAutoCreateFile()).isFalse();
        assertThat(config.getApplicationPath()).isEqualTo("path/path/directory");
        assertThat(config.isStrictFileReadMode()).isTrue();
        assertThat(config.getDirectoryLevelsToCheck()).isEqualTo(SharecartConfig.DEFAULT_LEVELS_TO_CHECK);
    }

    @Test
    public void testApplication_path_null_is_replaced_with_default() throws Exception {
        SharecartConfig config = configBuilder
                .setApplicationPath(null)
                .build();

        assertThat(config).isNotNull();
        assertThat(config.getApplicationPath()).isEqualTo(SharecartFileUtils.getApplicationPath());
    }

    @Test
    public void testLevels_negative_number_causes_exception() throws Exception {
        try {
            configBuilder.setDirectoryLevelsToCheck(-1)
                    .build();
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage("Levels to check cannot be a negative number. Number was -1");
        }
    }

    @Test
    public void testBuilder_is_reusable() throws Exception {
        SharecartConfig config1 = configBuilder.setClampToConstraints(true)
                .setAutoCreateFile(true)
                .setClampToConstraints(false)
                .setDirectoryLevelsToCheck(20)
                .setStrictFileReadMode(true)
                .setApplicationPath("a path")
                .build();

        SharecartConfig config2 = configBuilder.setAutoCreateFile(false)
                .setAutoCreateFile(false)
                .setClampToConstraints(true)
                .setApplicationPath("another path")
                .build();

        assertThat(config1).isNotNull();
        assertThat(config1.willAutoCreateFile()).isTrue();
        assertThat(config1.willClampToConstraints()).isFalse();
        assertThat(config1.getDirectoryLevelsToCheck()).isEqualTo(20);
        assertThat(config1.isStrictFileReadMode()).isTrue();
        assertThat(config1.getApplicationPath()).isEqualTo("a path");

        assertThat(config2).isNotNull();
        assertThat(config2.willAutoCreateFile()).isFalse();
        assertThat(config2.willClampToConstraints()).isTrue();
        assertThat(config2.getDirectoryLevelsToCheck()).isEqualTo(20);
        assertThat(config2.isStrictFileReadMode()).isTrue();
        assertThat(config2.getApplicationPath()).isEqualTo("another path");
    }
}

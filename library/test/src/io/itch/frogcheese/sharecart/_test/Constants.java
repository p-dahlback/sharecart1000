package io.itch.frogcheese.sharecart._test;

/**
 * Some constants used for the tests
 */
public class Constants {

    /**
     * The relative path to the test resources directory
     */
    public static final String TEST_RESOURCES_PATH = "test/resources/";

    /**
     * The relative path to the bottom of the subfolder tree in the test resources directory. This folder is used
     * primarily to test the framework's directory traversal when searching for .ini files.
     */
    public static final String TEST_EMPTY_SUBFOLDER_PATH = TEST_RESOURCES_PATH + "games/subfolder/subsubfolder";

    /**
     * The relative path to the /dat folder in the test resources directory.
     */
    public static final String TEST_DAT_FOLDER_PATH = TEST_RESOURCES_PATH + "dat/";

    /**
     * The relative path to the o_o.ini file in the test resources directory.
     */
    public static final String TEST_INI_FILE_PATH = TEST_DAT_FOLDER_PATH + "o_o.ini";

    /**
     * The relative path to the expected location of a generated o_o.ini file when traversing from the bottom
     * of the subfolder tree in the test resources directory.
     *
     * @see #TEST_EMPTY_SUBFOLDER_PATH
     */
    public static final String TEST_SUBFOLDER_INI_FILE_PATH = TEST_RESOURCES_PATH + "games/subfolder/dat/o_o.ini";
}

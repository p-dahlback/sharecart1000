package io.itch.frogcheese.sharecart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


abstract class ShareCartFileInterface {

    private static final String DAT_DIRECTORY = "dat";
    private static final String SHARECART_FILE = "o_o.ini";


    private static ShareCartFileInterface INSTANCE = null;


    public static ShareCartFileInterface get() {
        if(INSTANCE == null) {
            INSTANCE = new ShareCartFileInterfaceImpl();
        }
        return INSTANCE;
    }

    public static void inject(ShareCartFileInterface impl) {
        INSTANCE = impl;
    }

    public abstract ShareCartFileReader getNewSharecartFileReader(ShareCartFile file) throws FileNotFoundException;

    public abstract ShareCartFileWriter getNewSharecartFileWriter(ShareCartFile file) throws FileNotFoundException;

    public abstract ShareCartFile findIniFile(int directoryLevelsToCheck, String startingPath);

    public abstract ShareCartFile findOrCreateIniFile(int directoryLevelsToCheck, String startingPath);

    /**
     * Default implementation.
     */
    private static class ShareCartFileInterfaceImpl extends ShareCartFileInterface {
        @Override
        public ShareCartFileReader getNewSharecartFileReader(ShareCartFile file) throws FileNotFoundException {
            return new ShareCartFileReader(file.getFile());
        }

        @Override
        public ShareCartFileWriter getNewSharecartFileWriter(ShareCartFile file) throws FileNotFoundException {
            return new ShareCartFileWriter(file.getFile());
        }

        public ShareCartFile findIniFile(int directoryLevelsToCheck, String startingPath) {
            return findIniFile(directoryLevelsToCheck, startingPath, false);
        }

        public ShareCartFile findOrCreateIniFile(int directoryLevelsToCheck, String startingPath) {
            return findIniFile(directoryLevelsToCheck, startingPath, true);
        }

        private ShareCartFile findIniFile(int directoryLevelsToCheck, String startingPath, boolean createIfNotExists) {
            for (int i = 0; i <= directoryLevelsToCheck; i++) {
                File file = ShareCartFileUtils.getFileAboveDirectory(i, startingPath, DAT_DIRECTORY);
                if (file.exists()) {
                    file = new File(file, SHARECART_FILE);
                    if (file.exists()) {
                        return ShareCartFile.fromFile(file);
                    }
                    if (createIfNotExists) {
                        return createIniFile(file);
                    }
                }
            }
            if(createIfNotExists) {
                return createDatDirectory(directoryLevelsToCheck, startingPath);
            }
            return null;
        }

        private ShareCartFile createDatDirectory(int directoryLevelsToCheck, String startingPath) {
            for (int i = 0; i <= directoryLevelsToCheck; i++) {
                File dir = ShareCartFileUtils.getFileAboveDirectory(i, startingPath, "../");
                if (dir.exists() && dir.isDirectory()) {
                    File dat = new File(dir, DAT_DIRECTORY);
                    if (dat.mkdir()) {
                        File iniFile = new File(dat, SHARECART_FILE);
                        return createIniFile(iniFile);
                    }
                }
            }
            return null;
        }

        private ShareCartFile createIniFile(File file) {
            if (file.exists()) {
                return ShareCartFile.fromFile(file);
            } else {
                try {
                    if (file.createNewFile()) {
                        return ShareCartFile.fromAutoCreatedFile(file);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

    }
}

package io.itch.frogcheese.sharecart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public abstract class SharecartFileInterface {

    private static final String DAT_DIRECTORY = "dat";
    private static final String SHARECART_FILE = "o_o.ini";


    private static SharecartFileInterface INSTANCE = null;


    public static SharecartFileInterface get() {
        if(INSTANCE == null) {
            INSTANCE = new SharecartFileInterfaceImpl();
        }
        return INSTANCE;
    }

    public static void inject(SharecartFileInterface impl) {
        INSTANCE = impl;
    }

    public abstract SharecartFileReader getNewSharecartFileReader(SharecartFile file) throws FileNotFoundException;

    public abstract SharecartFileWriter getNewSharecartFileWriter(SharecartFile file) throws FileNotFoundException;

    public abstract SharecartFile findIniFile(int directoryLevelsToCheck, String startingPath);

    public abstract SharecartFile findOrCreateIniFile(int directoryLevelsToCheck, String startingPath);

    /**
     * Default implementation.
     */
    private static class SharecartFileInterfaceImpl extends SharecartFileInterface {
        @Override
        public SharecartFileReader getNewSharecartFileReader(SharecartFile file) throws FileNotFoundException {
            return new SharecartFileReader(file.getFile());
        }

        @Override
        public SharecartFileWriter getNewSharecartFileWriter(SharecartFile file) throws FileNotFoundException {
            return new SharecartFileWriter(file.getFile());
        }

        public SharecartFile findIniFile(int directoryLevelsToCheck, String startingPath) {
            return findIniFile(directoryLevelsToCheck, startingPath, false);
        }

        public SharecartFile findOrCreateIniFile(int directoryLevelsToCheck, String startingPath) {
            return findIniFile(directoryLevelsToCheck, startingPath, true);
        }

        private SharecartFile findIniFile(int directoryLevelsToCheck, String startingPath, boolean createIfNotExists) {
            for (int i = 0; i <= directoryLevelsToCheck; i++) {
                File file = SharecartFileUtils.getFileAboveDirectory(i, startingPath, DAT_DIRECTORY);
                if (file.exists()) {
                    file = new File(file, SHARECART_FILE);
                    if (file.exists()) {
                        return SharecartFile.fromFile(file);
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

        private SharecartFile createDatDirectory(int directoryLevelsToCheck, String startingPath) {
            for (int i = 0; i <= directoryLevelsToCheck; i++) {
                File dir = SharecartFileUtils.getFileAboveDirectory(i, startingPath, "../");
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

        private SharecartFile createIniFile(File file) {
            if (file.exists()) {
                return SharecartFile.fromFile(file);
            } else {
                try {
                    if (file.createNewFile()) {
                        return SharecartFile.fromAutoCreatedFile(file);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

    }
}

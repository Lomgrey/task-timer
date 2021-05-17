package com.tasktimer.repository.local.files;

import lombok.SneakyThrows;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;

public class OSDependLocalFile {

    public static File get() {
        if (SystemUtils.IS_OS_MAC) {
            return getMacOSLocalFile();
        }
        if (SystemUtils.IS_OS_WINDOWS) {
            return getWindowsLocalFile();
        }
        throw notImplementedOSException();
    }

    private static File getMacOSLocalFile() {
        return getOrCreateIfNotExist(MacOSLocalFile.localFile);
    }

    private static File getWindowsLocalFile() {
        return getOrCreateIfNotExist(WindowsOSLocalFile.localFile);
    }

    @SneakyThrows // todo: Handle exception and provide alternative to user
    public static File getOrCreateIfNotExist(File localFile) {
        if (localFile.exists()) {
            return localFile;
        }
        if (localFile.createNewFile()) {
            return localFile;
        } else {
            throw new RuntimeException("Can not create file at '" + localFile + "'");
        }
    }

    private static RuntimeException notImplementedOSException() {
        return new RuntimeException("No implementation for OS "
                + System.getProperty("os.name"));
    }

}

package com.tasktimer.repository.local.files;

import lombok.SneakyThrows;

import java.io.File;

public class MacOSLocalFile {

    private static final String path = "/Library/Application Support/Task Timer/days.json";
    static final File localFile = new File(path);

}

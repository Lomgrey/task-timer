package com.tasktimer.repository.local;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileCycleRepositoryTest {
    static final String baseResourcePath = "src/test/resources/";
    static final File daysFile = new File(baseResourcePath + "days.json");

    static LocalFileCycleRepository repository;

    @BeforeAll
    static void beforeAll() {
        repository = new LocalFileCycleRepository(daysFile);
    }

    /* LOADING test */
    @Test
    void testLoadingAndParseFile_fileExist() {
        File daysFile = new File(baseResourcePath + "days.json");
        new LocalFileCycleRepository(daysFile);
    }

    @Test
    void testLoadingAndParseFile_fileIsEmpty() throws IOException {
        var emptyFile = Files.createTempFile("tt", "").toFile();
        assert emptyFile.createNewFile();

        new LocalFileCycleRepository(emptyFile);

        assertTrue(emptyFile.exists());
        assertTrue(emptyFile.length() > 0);
    }

    @Test
    void testLoadingAndParseFile_fileWithDefaultValues() {
        File daysFile = new File(baseResourcePath + "with_default_values.json");
        new LocalFileCycleRepository(daysFile);
    }

    /* Functionality tests */


}
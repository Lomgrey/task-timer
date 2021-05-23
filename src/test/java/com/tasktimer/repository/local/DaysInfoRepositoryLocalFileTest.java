package com.tasktimer.repository.local;

import com.tasktimer.repository.mapper.ObjectMapperFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.tasktimer.repository.mapper.ObjectMapperFactory.MAPPER;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DaysInfoRepositoryLocalFileTest {
    static final String baseResourcePath = "src/test/resources/";
    static final File daysFile = new File(baseResourcePath + "days.json");

    static DaysInfoRepositoryLocalFile repository;

    @BeforeAll
    static void beforeAll() {
        repository = new DaysInfoRepositoryLocalFile(daysFile, ObjectMapperFactory.MAPPER);
    }

    /**
     * LOADING test
     */

    @Test
    void testLoadingAndParseFile_fileExist() {
        new DaysInfoRepositoryLocalFile(daysFile, MAPPER);
    }

    @Test
    void testLoadingAndParseFile_fileIsEmpty() throws IOException {
        var emptyFile = Files.createTempFile("tt", "").toFile();

        new DaysInfoRepositoryLocalFile(emptyFile, MAPPER);

        assertTrue(emptyFile.exists());
        assertTrue(emptyFile.length() > 0);
    }

    @Test
    void testLoadingAndParseFile_fileWithDefaultValues() {
        File daysFile = new File(baseResourcePath + "with_default_values.json");
        new DaysInfoRepositoryLocalFile(daysFile, MAPPER);
    }

    /**
     * Functionality tests
     */


}
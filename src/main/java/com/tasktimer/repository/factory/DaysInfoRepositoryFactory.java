package com.tasktimer.repository.factory;

import com.tasktimer.repository.DaysInfoRepository;
import com.tasktimer.repository.local.DaysInfoRepositoryLocalFile;
import com.tasktimer.repository.local.files.OSDependLocalFile;

import java.io.File;

import static com.tasktimer.repository.mapper.ObjectMapperFactory.MAPPER;

public class DaysInfoRepositoryFactory {

    private final static DaysInfoRepository SAYS_INFO_REPOSITORY;
    static {
        File localFile = OSDependLocalFile.get();
        SAYS_INFO_REPOSITORY = new DaysInfoRepositoryLocalFile(localFile, MAPPER);
    }

    public static DaysInfoRepository getInstance() {
        return SAYS_INFO_REPOSITORY;
    }
}

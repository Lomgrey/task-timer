package com.tasktimer.repository.mapper;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.util.Duration;

public class ObjectMapperFactory {

    public static final ObjectMapper MAPPER;
    static {
        MAPPER = new ObjectMapper();

        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.registerModule(durationFXSerializerModule());
        MAPPER.registerModule(durationFXDeserializerModule());

        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    static SimpleModule durationFXSerializerModule() {
        return new SimpleModule()
                .addSerializer(Duration.class, new DurationFXSerializer());
    }

    static SimpleModule durationFXDeserializerModule() {
        return new SimpleModule()
                .addDeserializer(Duration.class, new DurationFXDeserializer());
    }

}

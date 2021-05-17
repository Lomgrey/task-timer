package com.tasktimer.repository.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import javafx.util.Duration;

import java.io.IOException;

public class DurationFXDeserializer extends StdDeserializer<Duration> {
    public DurationFXDeserializer() {
        this(null);
    }

    protected DurationFXDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Duration deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String valueStr = p.getValueAsString();
        return Duration.millis(Double.parseDouble(valueStr));
    }
}

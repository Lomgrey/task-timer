package com.tasktimer.repository.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javafx.util.Duration;

import java.io.IOException;

public class DurationFXSerializer extends StdSerializer<Duration> {

    public DurationFXSerializer() {
        this(Duration.class);
    }

    public DurationFXSerializer(Class<Duration> t) {
        super(t);
    }

    @Override
    public void serialize(Duration value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(String.valueOf(value.toMillis()));
    }
}

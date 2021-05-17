package com.tasktimer.repository.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Duration;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectMapperTest {
    static final ObjectMapper mapper = ObjectMapperFactory.MAPPER;

    @Test
    void localDateDeserialization() throws JsonProcessingException {
        var dateStr = "2021-05-15";
        var json = "{\"date\": \"" + dateStr + "\"}";

        var instance = mapper.readValue(json, JsonType.class);

        assertEquals(LocalDate.parse(dateStr), instance.getDate());
    }

    @Test
    void localDateSerialization() throws JsonProcessingException {
        var dateStr = "2021-05-15";
        LocalDate date = LocalDate.parse(dateStr);
        JsonType instance = new JsonType().setDate(date);

        String serialized = mapper.writeValueAsString(instance);

        String expectedJson = "{\"date\":\"" + dateStr + "\"}";
        assertEquals(expectedJson, serialized);
    }

    @Test
    void durationSerialization() throws JsonProcessingException {
        Duration duration = Duration.millis(1.98E8); // 2 hours 34 mins 19 secs

        String serialised = mapper.writeValueAsString(duration);

        assertEquals("\"1.98E8\"", serialised);
    }

    @Test
    void durationDeserialization() throws JsonProcessingException {
        String durationVal = "1233123";

        Duration duration = mapper.readValue(durationVal, Duration.class);
        System.out.println(duration.toString());
    }

    @Test
    void name() {
        Duration a = Duration.millis(1233123);
        Duration b = Duration.millis(12312);
        Duration d = Duration.millis(222);

        System.out.println("a + b =" + a.add(b));
        System.out.println("a + b + d = " + a.add(b).add(d));
    }

    public static class JsonType {
        private LocalDate date;

        public LocalDate getDate() { return date; }
        public JsonType setDate(LocalDate date) {
            this.date = date;
            return this;
        }
    }
}
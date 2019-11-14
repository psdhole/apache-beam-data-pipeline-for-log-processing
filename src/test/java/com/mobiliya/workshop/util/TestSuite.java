package com.mobiliya.workshop.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestSuite {

    public static final String EVENT_PAYLOAD_JSON = "src/test/resources/cim-event_payload.json";

    public static final String EVENT_PAYLOAD_MALFORMED_JSON = "src/test/resources/cim-event_payload_error.json";

    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getEventPayloadJson(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}

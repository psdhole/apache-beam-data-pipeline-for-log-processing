package com.mobiliya.workshop.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.TupleTag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The {@link TestSuite} class used common for all the test classes.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestSuite {

    public static final String EVENT_PAYLOAD_JSON = "src/test/resources/cim-event_payload.json";

    public static final String EVENT_PAYLOAD_MALFORMED_JSON = "src/test/resources/cim-event_payload_error.json";

    public static final String EVENT_PAYLOAD_ERROR_META_JSON = "src/test/resources/cim-event_payload_error_meta.json";

    public static final TupleTag<KV<String, String>> FAILURE_TAG = new TupleTag<KV<String, String>>() {
    };

    public static final TupleTag<KV<String, String>> SUCCESS_TAG = new TupleTag<KV<String, String>>() {
    };
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getEventPayloadJson(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}

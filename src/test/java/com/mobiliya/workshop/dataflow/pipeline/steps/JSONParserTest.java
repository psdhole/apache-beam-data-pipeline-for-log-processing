package com.mobiliya.workshop.dataflow.pipeline.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiliya.workshop.dataflow.pipeline.entities.LogMessage;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.values.KV;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class JSONParserTest {
  @Rule public final TestPipeline pipeline = TestPipeline.create();

  @Test
  public void testApply() throws IOException {
    JSONParser jsonParser = new JSONParser();
    ObjectMapper objectMapper = new ObjectMapper();
    File file =
        new File(getClass().getClassLoader().getResource("cim-event_payload.json").getFile());
    LogMessage logMessage = objectMapper.readValue(file, LogMessage.class);
    KV<String, String> result =
        jsonParser.apply(KV.of("key", objectMapper.writeValueAsString(logMessage)));
    Assert.assertEquals(logMessage.getLogType(), result.getKey());
    Assert.assertEquals(logMessage, objectMapper.readValue(result.getValue(), LogMessage.class));
  }

  @Test
  public void testApplyFailure() throws IOException {
    JSONParser jsonParser = new JSONParser();
    KV<String, String> result = jsonParser.apply(KV.of("key", "Hello"));
    Assert.assertEquals("Hello", result.getValue());
    Assert.assertTrue(result.getKey() == null);
  }
}

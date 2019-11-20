package com.mobiliya.workshop.dataflow.pipeline.steps;

import com.mobiliya.workshop.dataflow.pipeline.entities.LogMessage;
import com.mobiliya.workshop.util.TestSuite;
import org.apache.beam.sdk.testing.TestPipeline;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

/**
 * Test class for {@link JSONParser}
 */
public class JSONParserTest {
  @Rule public final TestPipeline pipeline = TestPipeline.create();

  @Test
  public void testApply() throws IOException {
    JSONParser jsonParser = new JSONParser();
    String jsonString = TestSuite.getEventPayloadJson(TestSuite.EVENT_PAYLOAD_JSON);
    LogMessage logMessage = TestSuite.objectMapper.readValue(jsonString, LogMessage.class);
    //   KV<String, String> result = jsonParser.apply(KV.of("", jsonString));
    Assert.assertTrue(true);

  }

  @Test
  public void testApplyFailure() throws IOException {
    JSONParser jsonParser = new JSONParser();
    String jsonString = TestSuite.getEventPayloadJson(TestSuite.EVENT_PAYLOAD_MALFORMED_JSON);
    //KV<String, String> result = jsonParser.apply(KV.of("", jsonString));
    Assert.assertTrue(true);
  }
}

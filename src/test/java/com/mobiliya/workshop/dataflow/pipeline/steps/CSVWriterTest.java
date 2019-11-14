package com.mobiliya.workshop.dataflow.pipeline.steps;

import com.mobiliya.workshop.dataflow.pipeline.entities.LogMessage;
import com.mobiliya.workshop.util.TestSuite;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.values.KV;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

/**
 * Test class for {@link CSVWriter}
 */
public class CSVWriterTest {

  @Rule public final TestPipeline pipeline = TestPipeline.create();

  @Test
  public void testApply() throws IOException {
    CSVWriter csvWriter = new CSVWriter();
    String jsonString = TestSuite.getEventPayloadJson(TestSuite.EVENT_PAYLOAD_JSON);
    LogMessage logMessage = TestSuite.objectMapper.readValue(jsonString, LogMessage.class);
    String result = csvWriter.apply(KV.of("", jsonString));
    StringBuilder sb = new StringBuilder("");
    sb.append(logMessage.getLogType())
            .append(",")
            .append(logMessage.getLogSeverity())
            .append(",")
            .append(logMessage.getLogPriority())
            .append(",")
            .append(logMessage.getLogDescription());
    Assert.assertEquals(sb.toString(), result);
  }

  @Test
  public void testApplyFailure() throws IOException {
    CSVWriter csvWriter = new CSVWriter();
    String jsonString = TestSuite.getEventPayloadJson(TestSuite.EVENT_PAYLOAD_MALFORMED_JSON);
    String result = csvWriter.apply(KV.of("", jsonString));
    Assert.assertEquals("", result);
    Assert.assertTrue(result.isEmpty());
  }
}

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

public class CSVWriterTest {

  @Rule public final TestPipeline pipeline = TestPipeline.create();

  @Test
  public void testApply() throws IOException {
    CSVWriter csvWriter = new CSVWriter();
    ObjectMapper objectMapper = new ObjectMapper();
    File file =
        new File(getClass().getClassLoader().getResource("cim-event_payload.json").getFile());
    LogMessage logMessage = objectMapper.readValue(file, LogMessage.class);
    String result = csvWriter.apply(KV.of("key", objectMapper.writeValueAsString(logMessage)));
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
    String result = csvWriter.apply(KV.of("key", "Hello"));
    Assert.assertEquals("", result);
    Assert.assertTrue(result.isEmpty());
  }
}

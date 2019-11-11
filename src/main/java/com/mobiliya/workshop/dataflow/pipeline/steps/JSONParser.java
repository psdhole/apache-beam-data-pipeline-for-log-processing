package com.mobiliya.workshop.dataflow.pipeline.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiliya.workshop.dataflow.pipeline.entities.LogMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;

@Slf4j
public class JSONParser extends SimpleFunction<KV<String, String>, String> {
  private static final long serialVersionUID = 1L;

  @Override
  public String apply(KV<String, String> inputJSON) {
    LogMessage logMessage = new LogMessage();
    try {
      logMessage = new ObjectMapper().readValue(inputJSON.getValue(), LogMessage.class);
    } catch (Exception e) {
      log.debug("Error while parsing JSON :", e);
    }
    return String.join(
        ",",
        logMessage.getLogType(),
        logMessage.getLogSeverity(),
        logMessage.getLogPriority(),
        logMessage.getLogDescription());
  }
}

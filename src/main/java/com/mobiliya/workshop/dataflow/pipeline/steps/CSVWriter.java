package com.mobiliya.workshop.dataflow.pipeline.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiliya.workshop.dataflow.pipeline.entities.LogMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;

/**
 * Class CSVWriter to convert a JSON row into the CSV file row.
 */
@Slf4j
public class CSVWriter extends SimpleFunction<KV<String, String>, String> {
  private static final long serialVersionUID = 1L;

  @Override
  public String apply(KV<String, String> inputLogData) {
    StringBuilder sb = new StringBuilder();
    try {
      LogMessage logMessage =
              new ObjectMapper().readValue(inputLogData.getValue(), LogMessage.class);
      sb.append(logMessage.getLogType())
              .append(",")
              .append(logMessage.getLogSeverity())
              .append(",")
              .append(logMessage.getLogPriority())
              .append(",")
              .append(logMessage.getLogDescription());
    } catch (Exception e) {
      log.debug("Error while parsing JSON :", e);
    }
    return sb.toString();
  }
}

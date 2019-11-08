package com.mobiliya.workshop.dataflow.pipeline.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiliya.workshop.util.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;

import java.util.Map;

@Slf4j
public class JSONParser extends SimpleFunction<KV<String, String>, String> {
  private static final long serialVersionUID = 1L;

  @Override
  public String apply(KV<String, String> inputJSON) {
    String csvRow = "";
    try {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, String> map = mapper.readValue(inputJSON.getValue(), Map.class);
      String logType = map.get(CommonConstants.KEY_LOG_TYPE);
      String logSeverity = map.get(CommonConstants.KEY_LOG_SEVERITY);
      String logPriority = map.get(CommonConstants.KEY_LOG_PRIORITY);
      String logDescription = map.get(CommonConstants.KEY_LOG_DESC);
      csvRow = String.join(",", logType, logPriority, logSeverity, logDescription);
    } catch (Exception e) {
      log.debug("Error while parsing JSON :", e);
    }
    return csvRow;
  }
}

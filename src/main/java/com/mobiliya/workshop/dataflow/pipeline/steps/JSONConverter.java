package com.mobiliya.workshop.dataflow.pipeline.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mobiliya.workshop.exception.FailureMetaData;
import com.mobiliya.workshop.util.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;

/** Class JSONConverter to convert the java object of error metadata into the JSON payload */
@Slf4j
public class JSONConverter extends SimpleFunction<KV<String, FailureMetaData>, KV<String, String>> {
  private static final long serialVersionUID = 1L;

  @Override
  public KV<String, String> apply(KV<String, FailureMetaData> inputData) {
    String jsonString = "";
    try {
      jsonString = CommonConstants.objectMapper.writeValueAsString(inputData.getValue());
    } catch (JsonProcessingException e) {
      jsonString = "Bad error meta data";
      log.error("Error occured while converting failure meta data to json", e);
    }
    return KV.of(inputData.getKey(), jsonString);
  }
}

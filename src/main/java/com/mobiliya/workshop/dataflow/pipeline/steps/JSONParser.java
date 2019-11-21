package com.mobiliya.workshop.dataflow.pipeline.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiliya.workshop.dataflow.pipeline.entities.LogMessage;
import com.mobiliya.workshop.exception.FailureMetaData;
import com.mobiliya.workshop.exception.LogPipelineFailures;
import com.mobiliya.workshop.util.CommonConstants;
import com.mobiliya.workshop.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

/**
 * Class JSONParser to convert a JSON payload into the java object.
 */
@Slf4j
public class JSONParser extends DoFn<KV<String, String>, KV<String, String>> {
  private static final long serialVersionUID = 1L;

  @ProcessElement
  public void processElement(@Element KV<String, String> inputJSON, ProcessContext processContext) {
    LogMessage logMessage = new LogMessage();
    try {
      logMessage = new ObjectMapper().readValue(inputJSON.getValue(), LogMessage.class);
    } catch (Exception e) {
      FailureMetaData failureMetaData = CommonUtil.getDataValidationFailureResponse(JSONParser.class.toString(), e.getMessage(), inputJSON.getValue());
      processContext.output(LogPipelineFailures.FAILURE_TAG, KV.of(inputJSON.getKey(), failureMetaData));
    }
    processContext.output(CommonConstants.SUCCESS_TAG, KV.of(logMessage.getLogType(), inputJSON.getValue()));
  }
}

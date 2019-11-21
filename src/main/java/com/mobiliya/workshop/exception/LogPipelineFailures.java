package com.mobiliya.workshop.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mobiliya.workshop.util.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TupleTag;

import java.io.Serializable;

/** Collects and logs failed records from the pipeline. */
@Slf4j
public class LogPipelineFailures implements Serializable {

  public static final TupleTag<KV<String, FailureMetaData>> FAILURE_TAG =
      new TupleTag<KV<String, FailureMetaData>>() {};
  private static final String FAILURE_TEXT = "LogFailures";

  // Log the pipeline failures on queue
  public static void logFailuresToQueue(
      String brokerUrl,
      String outputTopic,
      final PCollection<KV<String, FailureMetaData>> failedRecords) {
    failedRecords
        .apply(
            "Convert the failure metadata to JSON",
            ParDo.of(
                new DoFn<KV<String, FailureMetaData>, KV<String, String>>() {
                  @ProcessElement
                  public void processElement(
                      @Element KV<String, FailureMetaData> inputJSON,
                      ProcessContext processContext) {
                    try {
                      processContext.output(
                          KV.of(
                              inputJSON.getKey(),
                              CommonConstants.objectMapper.writeValueAsString(
                                  inputJSON.getValue())));
                    } catch (JsonProcessingException e) {
                      log.error("Error occured while converting failure meta data to json", e);
                    }
                  }
                }))
        .apply(
            FAILURE_TEXT.concat("Write failed records to Kafka"),
            KafkaIO.<String, String>write()
                .withBootstrapServers(brokerUrl)
                .withTopic(outputTopic)
                .withKeySerializer(org.apache.kafka.common.serialization.StringSerializer.class)
                .withValueSerializer(org.apache.kafka.common.serialization.StringSerializer.class));
  }
}

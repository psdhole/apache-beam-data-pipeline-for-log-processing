package com.mobiliya.workshop.exception;

import com.mobiliya.workshop.dataflow.pipeline.steps.JSONConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import java.io.Serializable;

/** Collects and logs failed records from the pipeline. */
@Slf4j
public class LogPipelineFailures implements Serializable {

  private static final String FAILURE_TEXT = "LogFailures";

  // Log the pipeline failures on queue
  public static void logFailuresToQueue(
      String brokerUrl,
      String outputTopic,
      final PCollection<KV<String, FailureMetaData>> failedRecords) {
    failedRecords
        .apply("Convert the failure metadata to JSON", MapElements.via(new JSONConverter()))
        .apply(
            FAILURE_TEXT.concat("Write failed records to Kafka"),
            KafkaIO.<String, String>write()
                .withBootstrapServers(brokerUrl)
                .withTopic(outputTopic)
                .withKeySerializer(org.apache.kafka.common.serialization.StringSerializer.class)
                .withValueSerializer(org.apache.kafka.common.serialization.StringSerializer.class));
  }
}

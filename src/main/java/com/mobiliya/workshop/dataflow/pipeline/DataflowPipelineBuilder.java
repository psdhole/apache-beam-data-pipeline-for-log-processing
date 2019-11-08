package com.mobiliya.workshop.dataflow.pipeline;

import com.google.common.collect.ImmutableMap;
import com.mobiliya.workshop.dataflow.pipeline.options.DataPipelineOptions;
import com.mobiliya.workshop.dataflow.pipeline.steps.JSONParser;
import com.mobiliya.workshop.exception.DataPipelineException;
import com.mobiliya.workshop.util.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.windowing.*;
import org.apache.beam.sdk.values.KV;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.joda.time.Duration;

import java.io.Serializable;

@Slf4j
public class DataflowPipelineBuilder implements Serializable {
  public Pipeline createDataPipeline(String[] args) {
    log.debug("create data pipeline function is started");
    final DataPipelineOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(DataPipelineOptions.class);
    final String projectName = options.getProject();
    if (StringUtils.isEmpty(projectName)) {
      throw new DataPipelineException("Project is missing from pipeline options.");
    }
    final Pipeline pipeline = Pipeline.create(options);
    pipeline
        .apply(
            KafkaIO.<String, String>read()
                .withBootstrapServers(options.getKafkaBrokerUrl())
                .withTopic(options.getInputKafkaTopicName())
                .withKeyDeserializer(StringDeserializer.class)
                .withValueDeserializer(StringDeserializer.class)
                .updateConsumerProperties(
                    ImmutableMap.of(
                        CommonConstants.AUTO_OFFSET_RESET_KEY,
                        CommonConstants.AUTO_OFFSET_RESET_VALUE))
                .withoutMetadata())
        .apply(
            "Applying Fixed window to read stream from Kafka",
            Window.<KV<String, String>>into(
                    FixedWindows.of(Duration.standardMinutes(options.getFixedWindowLength())))
                .triggering(
                    Repeatedly.forever(
                        AfterFirst.of(
                            AfterPane.elementCountAtLeast(90),
                            AfterProcessingTime.pastFirstElementInPane()
                                .plusDelayOf(Duration.standardMinutes(2)))))
                .withAllowedLateness(Duration.ZERO)
                .discardingFiredPanes())
        .apply("Extract the JSON Fields", MapElements.via(new JSONParser()))
        .apply(
            TextIO.write()
                .withWindowedWrites()
                .withShardNameTemplate(CommonConstants.SHARDING_TEMPLATE_VALUE)
                .to(CommonConstants.OUTPUT_FILE_PREFIX)
                .withNumShards(options.getNumShards())
                .withSuffix(CommonConstants.OUTPUT_FILE_SUFFIX));
    log.debug("All done ..!!");
    return pipeline;
  }
}

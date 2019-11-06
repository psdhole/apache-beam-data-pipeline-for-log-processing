package com.mobiliya.workshop.dataflow.pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.transforms.windowing.*;
import org.apache.beam.sdk.values.KV;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;

@Slf4j
public class DataflowPipelineBuilder implements Serializable {
  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DataflowPipelineBuilder.class);

  /** The Constant BOOTSTRAP_SERVERS. */
  private static final String BOOTSTRAP_SERVERS = "broker:9092";

  /** The Constant INPUT_TOPIC. */
  private static final String INPUT_TOPIC = "input-log-topic";

  /** The Constant INPUT_TOPIC. */
  private static final String OUTPUT_FILE_NAME = "output";

  public Pipeline createDataPipeline(String[] args) {
    //
    log.debug("create data pipeline function is started");

    /*    final DataPipelineOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(DataPipelineOptions.class);
    final String projectName = options.getProject();
    if (StringUtils.isEmpty(projectName)) {
      throw new DataPipelineException("Project is missing from pipeline options.");
    }*/

    final PipelineOptions options =
        PipelineOptionsFactory.fromArgs().withValidation().as(PipelineOptions.class);
    Pipeline pipeline = Pipeline.create(options);

    pipeline
        .apply(
            KafkaIO.<String, String>read()
                .withBootstrapServers(BOOTSTRAP_SERVERS)
                .withTopic(INPUT_TOPIC)
                .withKeyDeserializer(StringDeserializer.class)
                .withValueDeserializer(StringDeserializer.class)
                .updateConsumerProperties(ImmutableMap.of("auto.offset.reset", "earliest"))
                .withoutMetadata())
        .apply(
            "Apply Fixed window: ",
            Window.<KV<String, String>>into(FixedWindows.of(Duration.standardMinutes(2)))
                .triggering(
                    Repeatedly.forever(
                        AfterFirst.of(
                            AfterPane.elementCountAtLeast(90),
                            AfterProcessingTime.pastFirstElementInPane()
                                .plusDelayOf(Duration.standardMinutes(2)))))
                .withAllowedLateness(Duration.ZERO)
                .discardingFiredPanes())
        .apply(
            MapElements.via(
                new SimpleFunction<KV<String, String>, String>() {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public String apply(KV<String, String> inputJSON) {
                    String csvRow = "";
                    try {
                      ObjectMapper mapper = new ObjectMapper();
                      Map<String, String> map = mapper.readValue(inputJSON.getValue(), Map.class);
                      String logType = map.get("logType");
                      String logSeverity = map.get("logSeverity");
                      String logPriority = map.get("logPriority");
                      String logDescription = map.get("logDescription");
                      csvRow = String.join(",", logType, logPriority, logSeverity, logDescription);
                    } catch (Exception e) {
                      LOGGER.debug("Error while parsing JSON :", e);
                    }
                    return csvRow;
                  }
                }))
        .apply(
            TextIO.write()
                .withWindowedWrites()
                .withShardNameTemplate("-logfile-SS-of-NN")
                .to(OUTPUT_FILE_NAME)
                .withNumShards(5)
                .withSuffix(".csv"));
    LOGGER.debug("All done ..!!");
    return pipeline;
  }
}

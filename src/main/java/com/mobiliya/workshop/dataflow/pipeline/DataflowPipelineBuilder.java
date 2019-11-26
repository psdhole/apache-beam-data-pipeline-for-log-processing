package com.mobiliya.workshop.dataflow.pipeline;

import com.google.common.collect.ImmutableMap;
import com.mobiliya.workshop.dataflow.pipeline.options.DataPipelineOptions;
import com.mobiliya.workshop.dataflow.pipeline.steps.CSVWriter;
import com.mobiliya.workshop.dataflow.pipeline.steps.JSONParser;
import com.mobiliya.workshop.exception.DataPipelineException;
import com.mobiliya.workshop.exception.FailureMetaData;
import com.mobiliya.workshop.exception.LogPipelineFailures;
import com.mobiliya.workshop.util.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTagList;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.joda.time.Duration;

import java.io.Serializable;

@Slf4j
public class DataflowPipelineBuilder implements Serializable {
  /**
   * Method to create a data pipeline based on options given by user.
   *
   * @param args contains pipeline options given by user.
   * @return data pipeline object.
   */
  public Pipeline createDataPipeline(String[] args) {
    log.debug("create data pipeline function is started");

    // Read pipeline options given from the command line.
    final DataPipelineOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(DataPipelineOptions.class);

    final String projectName = options.getProject();

    if (StringUtils.isEmpty(projectName)) {
      throw new DataPipelineException("Project is missing from pipeline options.");
    }

    // Create data pipeline with valid options.
    final Pipeline pipeline = Pipeline.create(options);

    // Get all the records from Kafka and identify valid and invalid data
    PCollectionTuple inputLogRecords =
        pipeline
            .apply(
                KafkaIO.<String, String>read()
                    .withBootstrapServers(options.getKafkaBrokerUrl())
                    .withTopic(options.getInputKafkaTopicName())
                    .withKeyDeserializer(StringDeserializer.class)
                    .withValueDeserializer(StringDeserializer.class)
                    .withConsumerConfigUpdates(
                        ImmutableMap.of(
                            CommonConstants.AUTO_OFFSET_RESET_KEY,
                            CommonConstants.AUTO_OFFSET_RESET_VALUE))
                    .withoutMetadata())
            .apply(
                "Parse input and create Key Pair",
                ParDo.of(new JSONParser())
                    .withOutputTags(
                        CommonConstants.SUCCESS_TAG, TupleTagList.of(CommonConstants.FAILURE_TAG)));

    // Get valid records to process.
    final PCollection<KV<String, String>> successRecords =
        inputLogRecords.get(CommonConstants.SUCCESS_TAG);

    // Process valid  records
    PCollection<KV<String, String>> recordsToEmitsToFile =
        successRecords.apply(
            "Applying Fixed window to read stream from Kafka",
            Window.<KV<String, String>>into(
                    FixedWindows.of(Duration.standardMinutes(options.getWindowSize())))
                .triggering(
                    Repeatedly.forever(
                        AfterFirst.of(
                            AfterPane.elementCountAtLeast(10),
                            AfterProcessingTime.pastFirstElementInPane()
                                .plusDelayOf(Duration.standardMinutes(2)))))
                .withAllowedLateness(Duration.ZERO)
                .discardingFiredPanes());

    // Convert each JSON string to the CSV record.
    PCollection<String> csvRecords =
        recordsToEmitsToFile.apply("Extract the JSON Fields", MapElements.via(new CSVWriter()));

    // Write the processed records to the CSV file
    csvRecords.apply(
        TextIO.write()
            .withWindowedWrites()
            .withHeader(CommonConstants.CSV_HEADERS)
            .withShardNameTemplate(CommonConstants.SHARDING_TEMPLATE_VALUE)
            .to(CommonConstants.OUTPUT_FILE_PREFIX)
            .withNumShards(options.getNumShards())
            .withSuffix(CommonConstants.OUTPUT_FILE_SUFFIX));

    // Get invalid valid records to process.
    final PCollection<KV<String, FailureMetaData>> failedRecords =
        inputLogRecords.get(CommonConstants.FAILURE_TAG);

    // Process invalid records
    LogPipelineFailures.logFailuresToQueue(
        options.getKafkaBrokerUrl(), options.getFailureDataTopic(), failedRecords);

    log.debug("Pipeline created successfully..!!");

    return pipeline;
  }
}

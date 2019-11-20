package com.mobiliya.workshop.dataflow.pipeline.options;

import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;

/**
 * Class to hold the data pipeline options values.
 */
public interface DataPipelineOptions extends GcpOptions {

  @Description("Topic name to handle failed log messages")
  @Default.String("failure-data-topic")
  String getFailureDataTopic();

  void setFailureDataTopic(String failureDataTopic);

  @Validation.Required
  @Description("Kafka Topic to read input payload from input kafka queue")
  String getInputKafkaTopicName();

  void setInputKafkaTopicName(String inputKafkaTopicName);

  @Validation.Required
  @Description("Kafka broker url to read payload from the input queue")
  String getKafkaBrokerUrl();

  void setKafkaBrokerUrl(String kafkaBrokerUrl);

  @Validation.Required
  @Description("Window size to read to read payload from Kafka input queue")
  int getWindowSize();

  void setWindowSize(int windowSize);

  @Validation.Required
  @Description("Number of shards to create while writing the processed data to the output file")
  int getNumShards();

  void setNumShards(int numShards);
}

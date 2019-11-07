package com.mobiliya.workshop.dataflow.pipeline.options;

import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;

public interface DataPipelineOptions extends GcpOptions {

  @Description("Topic to read payload from ingestion queue")
  String getCimEventsTopic();

  void setCimEventsTopic(String cimEventsTopic);

  @Description("Database URL command line argument.")
  String getDatabaseURL();

  void setDatabaseURL(String databaseURL);

  @Description("Database USERNAME command line argument.")
  String getDatabaseUserName();

  void setDatabaseUserName(String databaseUserName);

  @Description("Database PASSWORD command line argument.")
  String getDatabasePassword();

  void setDatabasePassword(String databasePassword);

  @Description("Failure Log Topic")
  String getFailureDataTopic();

  void setFailureDataTopic(String topic);

  @Validation.Required
  @Description("Kafka Topic to read input payload from ingestion queue")
  String getInputKafkaTopicName();

  void setInputKafkaTopicName(String inputKafkaTopicName);

  @Validation.Required
  @Description("Kafka broker url to read payload from ingestion queue")
  String getKafkaBrokerUrl();

  void setKafkaBrokerUrl(String kafkaBrokerUrl);

  @Validation.Required
  @Description("Window length to read to read payload from Kafka ingestion queue")
  int getFixedWindowLength();

  void setFixedWindowLength(int fixedWindowLength);
}

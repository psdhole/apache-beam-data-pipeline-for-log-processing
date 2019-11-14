package com.mobiliya.workshop.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Common constants used in test suite.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
  public static final String KAFKA_BROKER_URL_KEY = "kafkaBrokerUrl";
  public static final String KAFKA_BROKER_URL = "broker:9092";

  public static final String KAKFA_INPUT_TOPIC_KEY = "inputKafkaTopicName";
  public static final String KAKFA_INPUT_TOPIC = "input-log-topic";

  public static final String FIXED_WINDOW_SIZE_KEY = "windowSize";
  public static final String FIXED_WINDOW_SIZE = "2";

  public static final String NUM_SHARDS_KEY = "numShards";
  public static final String NUM_SHARDS_VALUE = "5";

  public static final String PROJECT_KEY = "project";
  public static final String PROJECT_ID = "test-isx-returns-09201971";

  public static final String RUNNER_KEY = "runner";
  public static final String RUNNER = "DirectRunner";

  public static final String PATTERN = "--%s=%s";
}

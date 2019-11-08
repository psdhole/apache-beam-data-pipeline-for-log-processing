package com.mobiliya.workshop.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  public static final String INGESTION_TOPIC_KEY = "cimEventsTopic";
  public static final String INGESTION_TOPIC = "cim-events";

  public static final String KAFKA_BROKER_URL_KEY = "kafkaBrokerUrl";
  public static final String KAFKA_BROKER_URL = "broker:9092";

  public static final String KAKFA_INPUT_TOPIC_KEY = "inputKafkaTopicName";
  public static final String KAKFA_INPUT_TOPIC = "input-log-topic";

  public static final String FIXED_WINDOW_LENGTH_KEY = "fixedWindowLength";
  public static final String FIXED_WINDOW_LENGTH = "2";

  public static final String DATABASE_URL_KEY = "databaseURL";
  public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";

  public static final String DATABASE_USER_NAME_KEY = "databaseUserName";
  public static final String DATABASE_USER_NAME = "postgres";

  public static final String DATABASE_PWD_KEY = "databasePassword";
  public static final String DATABASE_PWD = "root";

  public static final String PROJECT_KEY = "project";
  public static final String PROJECT_ID = "test-isx-returns-09201971";

  public static final String RUNNER_KEY = "runner";
  public static final String RUNNER = "DirectRunner";

  public static final String PATTERN = "--%s=%s";
}
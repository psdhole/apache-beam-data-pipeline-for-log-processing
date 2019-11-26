package com.mobiliya.workshop.common;

import com.mobiliya.workshop.dataflow.pipeline.entities.LogMessage;
import com.mobiliya.workshop.dataflow.pipeline.entities.LogType;
import com.mobiliya.workshop.util.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/** The Class {@link LogMessageProducer} to produces the messages on kakfa topic */
@Slf4j
public class LogMessageProducer {

  public static final String KEY_ACKS = "acks";
  public static final String VALUE_ACKS = "all";
  public static final String KEY_RETRIES = "retries";
  public static final String KEY_SERIALIZER = "key.serializer";
  public static final String BROKER_URL_KEY = "bootstrap.servers";
  public static final String BROKER_URL_VALUE = "broker:9092";
  public static final String VALUE_SERIALIZER = "value.serializer";
  public static final String STRING_SERIALIZER =
      "org.apache.kafka.common.serialization.StringSerializer";
  public static final String INPUT_TOPIC_NAME = "input-log-topic";

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    for (long i = 0; i < 1; i++) {
      int logPriority = 1;
      for (LogType logType : LogType.values()) {
        logPriority++;
        // send success records
        sendLogMessage(
            LogMessage.builder()
                .logType(logType.toString())
                .logSeverity(String.valueOf(logPriority))
                .logPriority(String.valueOf(logPriority))
                .logDescription("This is error no " + i + "" + logPriority)
                .build());
      }
      // send failed record
      sendLogMessage(new LogMessage());
    }
  }

  /**
   * Method to send log message on the kafka topic.
   *
   * @param message log message to send on kafka topic
   */
  private static void sendLogMessage(LogMessage message) {

    try (Producer<String, String> producer = getKafkaProducer()) {
      String logMessage = "";
      if (!Objects.isNull(message.getLogType())) {
        logMessage = CommonConstants.objectMapper.writeValueAsString(message);
      } else {
        message.setLogType(LogType.ERROR.toString());
        logMessage = getEventPayloadJson(CommonConstants.EVENT_PAYLOAD_MALFORMED_JSON);
      }
      producer.send(new ProducerRecord<>(INPUT_TOPIC_NAME, message.getLogType(), logMessage)).get();
    } catch (Exception e) {
      log.error("Error while sending message :", e);
    }
  }

  private static KafkaProducer getKafkaProducer() {
    Properties props = new Properties();
    props.put(BROKER_URL_KEY, BROKER_URL_VALUE);
    props.put(KEY_ACKS, VALUE_ACKS);
    props.put(KEY_RETRIES, 0);
    props.put(KEY_SERIALIZER, STRING_SERIALIZER);
    props.put(VALUE_SERIALIZER, STRING_SERIALIZER);
    return new KafkaProducer<>(props);
  }

  public static String getEventPayloadJson(String path) throws IOException {
    return new String(Files.readAllBytes(Paths.get(path)));
  }
}

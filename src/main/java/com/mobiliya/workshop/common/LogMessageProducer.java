/*
 *
 */
package com.mobiliya.workshop.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiliya.workshop.dataflow.pipeline.entities.LogMessage;
import com.mobiliya.workshop.dataflow.pipeline.entities.LogType;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * The Class {@link LogMessageProducer} to produces the messages on kakfa topic
 */
@Slf4j
public class LogMessageProducer {

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    for (long i = 0; i < 100; i++) {
      sendLog(
          LogMessage.builder()
              .logType(LogType.INFO.toString())
              .logSeverity("1")
              .logPriority("1")
              .logDescription("NEWMSG111" + i)
              .build());
      sendLog(
          LogMessage.builder()
              .logType(LogType.DEBUG.toString())
              .logSeverity("2")
              .logPriority("2")
              .logDescription("NEWMSG222" + i)
              .build());
      sendLog(
          LogMessage.builder()
              .logType(LogType.WARNING.toString())
              .logSeverity("3")
              .logPriority("3")
              .logDescription("NEWMSG333" + i)
              .build());
      sendLog(
          LogMessage.builder()
              .logType(LogType.TRACE.toString())
              .logSeverity("4")
              .logPriority("4")
              .logDescription("NEWMSG444" + i)
              .build());
      sendLog(
          LogMessage.builder()
              .logType(LogType.ERROR.toString())
              .logSeverity("5")
              .logPriority("5")
              .logDescription("NEWMSG555" + i)
              .build());
      sendLog(
          LogMessage.builder()
              .logType(LogType.FATAL.toString())
                  .logSeverity("5")
                  .logPriority("5")
              .logDescription("NEWMSG666" + i)
              .build());
    }
  }

  /**
   * Method to send log message on the kafka topic.
   *
   * @param message log message to send on kafka topic
   */
  private static void sendLog(LogMessage message) {
    String topic = "input-log-topic";
    Properties props = new Properties();
    props.put("bootstrap.servers", "broker:9092");
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    ObjectMapper mapper = new ObjectMapper();
    try (Producer<String, String> producer = new KafkaProducer<>(props)) {
      String logMessage = mapper.writeValueAsString(message);
      ProducerRecord<String, String> logMessageRecordToSend =
          new ProducerRecord<>(topic, message.getLogType(), logMessage);
      Thread.sleep(5000);
      log.debug("Sending log message: {}", logMessage);
      producer.send(logMessageRecordToSend).get();
    } catch (Exception e) {
      log.error("error while sending message :", e);
    }
  }
}

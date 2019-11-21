package com.mobiliya.workshop.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.TupleTag;

/**
 * Common used in the pipeline application.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstants {
  public static final String SHARDING_TEMPLATE_VALUE = "-logfile-SS-of-NN";
  public static final String OUTPUT_FILE_PREFIX = "output";
  public static final String OUTPUT_FILE_SUFFIX = ".csv";
  public static final String AUTO_OFFSET_RESET_KEY = "auto.offset.reset";
  public static final String AUTO_OFFSET_RESET_VALUE = "earliest";
  public static final String CSV_HEADERS = "LogType,LogSeverity,LogPriority,LogDescription";
  public static final ObjectMapper objectMapper = new ObjectMapper();
  public static final String EVENT_PAYLOAD_MALFORMED_JSON = "src/test/resources/cim-event_payload_error.json";
  public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSXXX";
  public static final TupleTag<KV<String, String>> FAILURE_TAG = new TupleTag<KV<String, String>>() {
  };
  public static final TupleTag<KV<String, String>> SUCCESS_TAG = new TupleTag<KV<String, String>>() {
  };
}

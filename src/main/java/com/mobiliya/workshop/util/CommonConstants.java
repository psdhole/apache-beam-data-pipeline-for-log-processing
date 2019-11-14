package com.mobiliya.workshop.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
  public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSXXX";
}

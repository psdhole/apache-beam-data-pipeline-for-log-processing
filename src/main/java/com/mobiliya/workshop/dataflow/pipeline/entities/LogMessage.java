package com.mobiliya.workshop.dataflow.pipeline.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** The class {@link LogMessage} */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMessage {
  private String logType;
  private String logSeverity;
  private String logPriority;
  private String logDescription;
}

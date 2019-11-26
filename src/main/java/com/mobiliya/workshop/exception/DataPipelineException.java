package com.mobiliya.workshop.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataPipelineException extends RuntimeException {

  public DataPipelineException(String message) {
    super(message);
  }
}

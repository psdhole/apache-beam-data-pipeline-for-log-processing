package com.mobiliya.workshop.dataflow.pipeline;

import com.mobiliya.workshop.dataflow.pipeline.options.DataPipelineOptions;
import com.mobiliya.workshop.exception.DataPipelineException;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Slf4j
public class DataflowPipelineBuilder implements Serializable {

  public Pipeline createDataPipeline(String[] args) {
    //
    log.debug("create data pipeline function is started");

    final DataPipelineOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(DataPipelineOptions.class);
    final String projectName = options.getProject();
    if (StringUtils.isEmpty(projectName)) {
      throw new DataPipelineException("Project is missing from pipeline options.");
    }

    // Create the Pipeline with the specified options
    final Pipeline pipeline = Pipeline.create(options);

    return pipeline;
  }
}

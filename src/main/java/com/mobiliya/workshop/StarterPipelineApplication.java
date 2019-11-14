package com.mobiliya.workshop;

import com.mobiliya.workshop.dataflow.pipeline.DataflowPipelineBuilder;

/**
 * The Main class to run the data pipeline.
 **
 */
public class StarterPipelineApplication {

  public static void main(String[] args) {
    new DataflowPipelineBuilder().createDataPipeline(args).run();
  }
}

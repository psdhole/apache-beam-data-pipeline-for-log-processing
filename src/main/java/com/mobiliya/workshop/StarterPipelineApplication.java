package com.mobiliya.workshop;

import com.mobiliya.workshop.dataflow.pipeline.DataflowPipelineBuilder;

/**
 * Provide description here
 * @version 0.1
 */
public class StarterPipelineApplication {

    public static void main(String[] args) {
        new DataflowPipelineBuilder().createDataPipeline(args).run();
    }
}

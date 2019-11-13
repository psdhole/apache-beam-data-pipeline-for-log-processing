package com.mobiliya.workshop.dataflow.pipeline;

import com.mobiliya.workshop.dataflow.pipeline.options.DataPipelineOptions;
import com.mobiliya.workshop.exception.DataPipelineException;
import com.mobiliya.workshop.util.Constants;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.testing.TestPipeline;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DataFlowPipelineBuilderTest {

  @Rule public final TestPipeline pipeline = TestPipeline.create();

  @Test
  public void testDataFlowPipeline() {

    Map<String, String> arguments = new HashMap<>();
    arguments.put(Constants.PROJECT_KEY, Constants.PROJECT_ID);
    arguments.put(Constants.DATABASE_URL_KEY, Constants.DATABASE_URL);
    arguments.put(Constants.DATABASE_USER_NAME_KEY, Constants.DATABASE_USER_NAME);
    arguments.put(Constants.DATABASE_PWD_KEY, Constants.DATABASE_PWD);
    arguments.put(Constants.INGESTION_TOPIC_KEY, Constants.INGESTION_TOPIC);
    arguments.put(Constants.KAFKA_BROKER_URL_KEY, Constants.KAFKA_BROKER_URL);
    arguments.put(Constants.KAKFA_INPUT_TOPIC_KEY, Constants.KAKFA_INPUT_TOPIC);
    arguments.put(Constants.FIXED_WINDOW_LENGTH_KEY, Constants.FIXED_WINDOW_LENGTH);
    arguments.put(Constants.NUM_SHARDS_KEY, Constants.NUM_SHARDS_VALUE);
    // To logs the failure / exception data

    arguments.put(Constants.RUNNER_KEY, Constants.RUNNER);
    DataflowPipelineBuilder builder = new DataflowPipelineBuilder();

    Pipeline actualPipeline =
        builder.createDataPipeline(
            arguments.entrySet().stream()
                .map(e -> String.format(Constants.PATTERN, e.getKey(), e.getValue()))
                .toArray(String[]::new));
    Assert.assertNotNull(actualPipeline);
    DataPipelineOptions options = (DataPipelineOptions) actualPipeline.getOptions();
    Assert.assertEquals(arguments.get(Constants.PROJECT_KEY), options.getProject());
    Assert.assertEquals(arguments.get(Constants.DATABASE_URL_KEY), options.getDatabaseURL());
    Assert.assertEquals(
        arguments.get(Constants.DATABASE_USER_NAME_KEY), options.getDatabaseUserName());
    Assert.assertEquals(arguments.get(Constants.DATABASE_PWD_KEY), options.getDatabasePassword());
    Assert.assertEquals(arguments.get(Constants.INGESTION_TOPIC_KEY), options.getCimEventsTopic());
    Assert.assertEquals(arguments.get(Constants.RUNNER_KEY), options.getRunner().getSimpleName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDataFlowPipelineWithoutTopic() {

    Map<String, String> arguments = new HashMap<>();
    arguments.put(Constants.PROJECT_KEY, Constants.PROJECT_ID);
    arguments.put(Constants.DATABASE_URL_KEY, Constants.DATABASE_URL);
    arguments.put(Constants.DATABASE_USER_NAME_KEY, Constants.DATABASE_USER_NAME);
    arguments.put(Constants.DATABASE_PWD_KEY, Constants.DATABASE_PWD);

    // To logs the failure / exception data
    arguments.put(Constants.RUNNER_KEY, Constants.RUNNER);

    DataflowPipelineBuilder sut = new DataflowPipelineBuilder();
    sut.createDataPipeline(
        arguments.entrySet().stream()
            .map(e -> String.format(Constants.PATTERN, e.getKey(), e.getValue()))
            .toArray(String[]::new));
  }

  @Test(expected = DataPipelineException.class)
  public void testDataFlowPipelineWithoutProject() {

    Map<String, String> arguments = new HashMap<>();
    arguments.put(Constants.DATABASE_URL_KEY, Constants.DATABASE_URL);
    arguments.put(Constants.DATABASE_USER_NAME_KEY, Constants.DATABASE_USER_NAME);
    arguments.put(Constants.DATABASE_PWD_KEY, Constants.DATABASE_PWD);
    arguments.put(Constants.INGESTION_TOPIC_KEY, Constants.INGESTION_TOPIC);
    arguments.put(Constants.KAFKA_BROKER_URL_KEY, Constants.KAFKA_BROKER_URL);
    arguments.put(Constants.KAKFA_INPUT_TOPIC_KEY, Constants.KAKFA_INPUT_TOPIC);
    arguments.put(Constants.FIXED_WINDOW_LENGTH_KEY, Constants.FIXED_WINDOW_LENGTH);
    arguments.put(Constants.NUM_SHARDS_KEY, Constants.NUM_SHARDS_VALUE);

    // To logs the failure / exception data
    arguments.put(Constants.RUNNER_KEY, Constants.RUNNER);

    DataflowPipelineBuilder sut = new DataflowPipelineBuilder();
    sut.createDataPipeline(
        arguments.entrySet().stream()
            .map(e -> String.format(Constants.PATTERN, e.getKey(), e.getValue()))
            .toArray(String[]::new));
  }
}

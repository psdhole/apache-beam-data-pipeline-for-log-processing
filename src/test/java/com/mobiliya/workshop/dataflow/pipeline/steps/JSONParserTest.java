package com.mobiliya.workshop.dataflow.pipeline.steps;

import com.mobiliya.workshop.dataflow.pipeline.entities.LogMessage;
import com.mobiliya.workshop.exception.FailureMetaData;
import com.mobiliya.workshop.util.CommonConstants;
import com.mobiliya.workshop.util.TestSuite;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTagList;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

/** Test class for {@link JSONParser} */
@Slf4j
public class JSONParserTest {
  @Rule public final TestPipeline pipeline = TestPipeline.create();

  @Test
  public void testJSONParserSuccess() throws IOException {
    String jsonString = TestSuite.getEventPayloadJson(TestSuite.EVENT_PAYLOAD_JSON);
    LogMessage logMessage = TestSuite.objectMapper.readValue(jsonString, LogMessage.class);
    PCollectionTuple result =
        pipeline
            .apply("Create", Create.of(KV.of(logMessage.getLogType(), jsonString)))
            .apply(
                "Conversion",
                ParDo.of(new JSONParser())
                    .withOutputTags(
                        CommonConstants.SUCCESS_TAG, TupleTagList.of(CommonConstants.FAILURE_TAG)));
    Assert.assertNotNull(result);

    final PCollection<KV<String, FailureMetaData>> failedRecords =
        result.get(CommonConstants.FAILURE_TAG);
    final PCollection<KV<String, String>> successRecords = result.get(CommonConstants.SUCCESS_TAG);

    PAssert.that(failedRecords).empty();
    Assert.assertFalse(successRecords.expand().isEmpty());

    PAssert.that(successRecords).containsInAnyOrder(KV.of(logMessage.getLogType(), jsonString));
    pipeline.run();
  }

  @Test
  public void testJSONParserFailure() throws IOException {
    String jsonString = TestSuite.getEventPayloadJson(TestSuite.EVENT_PAYLOAD_MALFORMED_JSON);
    String metaJsonString = TestSuite.getEventPayloadJson(TestSuite.EVENT_PAYLOAD_ERROR_META_JSON);
    FailureMetaData failureMetaData =
        CommonConstants.objectMapper.readValue(metaJsonString, FailureMetaData.class);
    PCollectionTuple result =
        pipeline
            .apply("Create", Create.of(KV.of("ERROR", jsonString)))
            .apply(
                "Conversion",
                ParDo.of(new JSONParser())
                    .withOutputTags(
                        CommonConstants.SUCCESS_TAG, TupleTagList.of(CommonConstants.FAILURE_TAG)));
    Assert.assertNotNull(result);

    final PCollection<KV<String, FailureMetaData>> failedRecords =
        result.get(CommonConstants.FAILURE_TAG);
    final PCollection<KV<String, String>> successRecords = result.get(CommonConstants.SUCCESS_TAG);

    Assert.assertFalse(failedRecords.expand().isEmpty());
    PAssert.that(successRecords).empty();
    pipeline.run();
  }
}

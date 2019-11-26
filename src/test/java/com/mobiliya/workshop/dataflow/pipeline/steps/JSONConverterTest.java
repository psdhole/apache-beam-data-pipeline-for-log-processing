package com.mobiliya.workshop.dataflow.pipeline.steps;

import com.mobiliya.workshop.exception.FailureMetaData;
import com.mobiliya.workshop.util.TestSuite;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.values.KV;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.omg.CORBA.Object;

import java.io.IOException;

/** Test class for {@link CSVWriter} */
public class JSONConverterTest {

  @Rule public final TestPipeline pipeline = TestPipeline.create();

  @Test
  public void testApply() throws IOException {
    JSONConverter jsonConverter = new JSONConverter();
    String jsonString =
        TestSuite.objectMapper.writeValueAsString(FailureMetaData.builder().build());
    KV<String, String> result = jsonConverter.apply(KV.of("", FailureMetaData.builder().build()));
    Assert.assertEquals(jsonString, result.getValue());
  }
}

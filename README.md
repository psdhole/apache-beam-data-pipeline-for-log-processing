# data-pipeline
Data pipline application to read error log data from Kafka and apply Composite Triggers to emit the results to the csv file based on below conditions.
   
    1 : If the error count is 10 OR 
    2 : If high severity (severity-1) error occurs OR 
    3 : If high Priority (priority-1) error occurs. 


### Deploy Project Locally
In a development environment, use the following call to  cleanly build and install artifacts into the local repository.
```sh
$ mvn clean install
```

Skip the tests via command line by executing the following command:
```sh
$ mvn install -DskipTests
```

Run/start the Kafka broker locally using below command. 
```sh
$ docker-compose up -d
```

Run pipeline locally
```sh
$ mvn -e -Pdirect-runner compile exec:java -Dexec.mainClass=com.mobiliya.workshop.pipeline.DataflowPipelineBuilder.StarterPipelineApplication -Dexec.args="--project=dev  --ingestionTopic=ingestion_dev --failureDataTopic=_dev_failure_data  --runner=DirectRunner --windowSize=2 --kafkaBrokerUrl=localhost:9092 --inputKafkaTopicName=input-log-topic --numShards=5"
```
Analyze project with SonarQube Server
```sh
$ mvn clean test
```

### Deploy Project on Dataflow Service
```sh
$ datapipeline exec:java -Dexec.mainClass=com.mobiliya.workshop.StarterPipelineApplication  -Dexec.args="--project=dev --stagingLocation=gs://project/staging/  --gcpTempLocation=gs://project/tmp --region=europe-west1 --zone=europe-west1-d --jobName=datapipeline-dev-v0 --autoscalingAlgorithm=THROUGHPUT_BASED --maxNumWorkers=15 --usePublicIps=true --saveProfilesToGcs=gs://project/profiling --databaseURL=jdbc:postgresql://google/postgres?cloudSqlInstance=dev&socketFactory=com.google.cloud.sql.postgres.SocketFactory&user=isx_dev&password=Pa55word$ --databaseUserName=dev --databasePassword=Pa55word$ --ingestionTopic=ingestion_dev  --failureDataTopic=dev_failure_data --runner=DataflowRunner"```
```

##### Notes:

  - The build process makes use of Embedded PostgreSQL Component for mocking and running the integration test cases related to PostgreSQL database. If you experience difficulty running otj-pg-embedded tests on Windows, make sure you've installed the appropriate MFC redistributables.
Download Visual C++ Redistributable Packages for Visual Studio 2013 using the following link.
[Visual C++ Redistributable Packages 2013](https://www.microsoft.com/en-ca/download/details.aspx?id=40784)
Choose the file name ' vcredist_x86.exe ' and proceed to install it.
After it's installed successfully, run mvn clean install command to build the project using clean option.

  -  Publish messages on the ingestion topic using key(´tracking_id_data_ingester`) as event processor job rely on key to read the message from topic to address deduplication issue. This is done by using [withIdAttribute API](https://beam.apache.org/releases/javadoc/2.4.0/org/apache/beam/sdk/io/gcp/pubsub/PubsubIO.Write.html#withIdAttribute-java.lang.String-).

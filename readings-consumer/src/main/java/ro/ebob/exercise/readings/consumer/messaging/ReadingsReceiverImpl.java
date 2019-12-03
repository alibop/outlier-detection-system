package ro.ebob.exercise.readings.consumer.messaging;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;

import com.google.common.math.Quantiles;

import ro.ebob.exercise.readings.consumer.domain.ReadingsMessage;
import ro.ebob.exercise.readings.data.domain.ReadingsEntity;
import ro.ebob.exercise.readings.data.service.ReadingsDataService;

@EnableBinding(Sink.class)
public class ReadingsReceiverImpl implements ReadingsReceiver {

  public static final String ISO8601_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";
  private static final Logger LOG = LoggerFactory.getLogger(ReadingsReceiverImpl.class);
  private final ReadingsDataService dataService;
  
  public ReadingsReceiverImpl(ReadingsDataService dataService) {
    this.dataService = dataService;
  }
  
  @StreamListener(Sink.INPUT)
  public void receive(@Payload ReadingsMessage data) {
      LOG.info("Recieved payload : [{}]: {} : {}", data.getTime(), data.getPublisher(), data.getReadings());
      saveToDb(data);
      LOG.info("...and saved to db");
  }
  
  private ReadingsEntity saveToDb(ReadingsMessage data) {
    Collections.sort(data.getReadings());
    return dataService.save(new ReadingsEntity(data.getPublisher(), data.getTime(), Quantiles.median().compute(data.getReadings())));
  }
}

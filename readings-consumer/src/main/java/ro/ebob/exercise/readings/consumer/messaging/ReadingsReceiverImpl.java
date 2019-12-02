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
import ro.ebob.exercise.readings.consumer.domain.MedianEntity;
import ro.ebob.exercise.readings.consumer.service.MedianDataService;

@EnableBinding(Sink.class)
public class ReadingsReceiverImpl implements ReadingsReceiver {

  public static final String ISO8601_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";
  private static final Logger LOG = LoggerFactory.getLogger(ReadingsReceiverImpl.class);
  private final MedianDataService dataService;
  
  public ReadingsReceiverImpl(MedianDataService dataService) {
    this.dataService = dataService;
  }
  
  @StreamListener(Sink.INPUT)
  public void receive(@Payload ReadingsMessage data) {
      LOG.info("Recieved payload : [{}]: {} : {}", data.getTime(), data.getPublisher(), data.getReadings());
      saveToDb(data);
      LOG.info("...and saved to db");
  }
  
  private MedianEntity saveToDb(ReadingsMessage data) {
    Collections.sort(data.getReadings());
    return dataService.save(new MedianEntity(data.getPublisher(), data.getTime(), Quantiles.median().compute(data.getReadings())));
  }
}

package ro.ebob.exercise.readings.web;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/readings")
public class ReadingsController {

  private final ReadingsSender readingsOut;
  
  public ReadingsController(ReadingsSender readingsOut) {
    this.readingsOut = readingsOut;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void publishReadings(@RequestBody PubReadings data) {
    data.setTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.of("UTC")).format(Instant.now()));
    readingsOut.send(data);
  }
}

class PubReadings {
  private String publisher;
  private String time;
  public String getPublisher() {
    return publisher;
  }
  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }
  public String getTime() {
    return time;
  }
  public void setTime(String time) {
    this.time = time;
  }
  public Integer[] getReadings() {
    return readings;
  }
  public void setReadings(Integer[] readings) {
    this.readings = readings;
  }
  private Integer[] readings;
}

@EnableBinding(Source.class)
class ReadingsSender {

  private final MessageChannel output;
  
  public ReadingsSender(Source source) {
    this.output = source.output();
  }
  
  public void send(PubReadings data) {
    output.send(MessageBuilder.withPayload(data).build());
  }

}

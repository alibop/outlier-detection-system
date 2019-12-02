package ro.ebob.exercise.readings.consumer.domain;

import java.util.List;

public class ReadingsMessage {
  
  private String publisher;
  private String time;
  private List<Integer> readings;

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

  public List<Integer> getReadings() {
    return readings;
  }

  public void setReadings(List<Integer> readings) {
    this.readings = readings;
  }
}

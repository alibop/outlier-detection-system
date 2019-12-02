package ro.ebob.exercise.readings.consumer.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MedianEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String publisher;
  private String time;
  private Double median;

  public MedianEntity() {
  }

  public MedianEntity(String publisher, String time, Double median) {
    this.publisher = publisher;
    this.setTime(time);
    this.median = median;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public Double getMedian() {
    return median;
  }

  public void setMedian(Double median) {
    this.median = median;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

}

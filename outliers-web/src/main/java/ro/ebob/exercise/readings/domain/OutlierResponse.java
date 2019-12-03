package ro.ebob.exercise.readings.domain;

import ro.ebob.exercise.readings.data.domain.ReadingsEntity;

public class OutlierResponse extends ReadingsEntity {
  
  private boolean outlier;
  
  public OutlierResponse of(ReadingsEntity original){
    this.setId(original.getId());
    this.setPublisher(original.getPublisher());
    this.setTime(original.getTime());
    this.setMedian(original.getMedian());
    return this;
  }
  
  public OutlierResponse outlier(boolean outlier){
    this.outlier = outlier;
    return this;
  }

  public boolean isOutlier() {
    return outlier;
  }

  public void setOutlier(boolean outlier) {
    this.outlier = outlier;
  }
}
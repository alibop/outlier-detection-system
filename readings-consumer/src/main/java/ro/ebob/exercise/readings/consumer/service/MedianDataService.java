package ro.ebob.exercise.readings.consumer.service;

import java.util.List;

import ro.ebob.exercise.readings.consumer.domain.MedianEntity;

public interface MedianDataService {

  List<MedianEntity> last(int n);
  List<MedianEntity> last(String publisher, int n);
  MedianEntity get(Long id);
  MedianEntity save(MedianEntity entity);
}

package ro.ebob.exercise.readings.data.service;

import java.util.List;

import ro.ebob.exercise.readings.data.domain.ReadingsEntity;

public interface ReadingsDataService {

  List<ReadingsEntity> last(int n);
  List<ReadingsEntity> last(String publisher, int n);
  ReadingsEntity get(Long id);
  ReadingsEntity save(ReadingsEntity entity);
}

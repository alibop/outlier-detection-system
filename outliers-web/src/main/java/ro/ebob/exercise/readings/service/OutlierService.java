package ro.ebob.exercise.readings.service;

import java.util.List;

import ro.ebob.exercise.readings.data.domain.ReadingsEntity;
import ro.ebob.exercise.readings.domain.OutlierResponse;

public interface OutlierService {
  List<OutlierResponse> markOutliers(List<ReadingsEntity> records);
}
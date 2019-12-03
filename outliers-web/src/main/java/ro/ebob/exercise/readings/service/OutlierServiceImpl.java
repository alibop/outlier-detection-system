package ro.ebob.exercise.readings.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.math.Quantiles;

import ro.ebob.exercise.readings.data.domain.ReadingsEntity;
import ro.ebob.exercise.readings.domain.OutlierResponse;

@Service
public class OutlierServiceImpl implements OutlierService {
  
  public static final Logger LOG = LoggerFactory.getLogger(OutlierServiceImpl.class);
  
  public List<OutlierResponse> markOutliers(List<ReadingsEntity> records) {
    if(records.isEmpty()) {
      return Collections.emptyList();
    }
    Double[] range = interquartileRangeReadings(records);
    LOG.info("Range: {} {}", range[0], range[1]);
    return records.stream().map(r -> new OutlierResponse()
        .of(r)
        .outlier(r.getMedian() < range[0] || r.getMedian() > range[1]))
        .collect(Collectors.toList());
  }
  
  protected Double[] interquartileRangeReadings(List<ReadingsEntity> records) {
    List<Double> values = records.stream().map(r -> r.getMedian()).collect(Collectors.toList());
    return interquartileRange(values);//[low,high]
  }
  
  protected Double[] interquartileRange(List<Double> values) {
    Collections.sort(values);
    double q1 = Quantiles.percentiles().index(25).compute(values);
    double q3 = Quantiles.percentiles().index(75).compute(values);
    return new Double[] {q1 - 1.5 * (q3 - q1), q3 + 1.5 * (q3 - q1)};//[low,high]
  }

}
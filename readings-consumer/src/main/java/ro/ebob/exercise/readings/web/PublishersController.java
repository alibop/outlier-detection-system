package ro.ebob.exercise.readings.web;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.math.Quantiles;

import ro.ebob.exercise.readings.consumer.domain.MedianEntity;
import ro.ebob.exercise.readings.consumer.service.MedianDataService;

@RestController
@RequestMapping("/publishers")
public class PublishersController {

  private MedianDataService dataService;
  private OutlierService outlierService;

  public PublishersController(MedianDataService dataService, OutlierService outlierService) {
    this.dataService = dataService;
    this.outlierService = outlierService;
  }
  
  @GetMapping("/{publisher}/outliers")
  @ResponseStatus(HttpStatus.OK)
  public List<OutlierResponse> retrievePublisherRecords(@PathVariable String publisher, @RequestParam(required=false, defaultValue="10") Integer limit) {
    return outlierService.markOutliers(dataService.last(publisher, limit));
  }
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<MedianEntity> retrieveRecords(@RequestParam(required=false, defaultValue="10") Integer limit) {
    return dataService.last(limit);
  }
}

@Service
class OutlierService {
  
  List<OutlierResponse> markOutliers(List<MedianEntity> records) {
    if(records.isEmpty()) {
      return Collections.emptyList();
    }
    Double[] range = interquartileRange(records);
    return records.stream().map(r -> new OutlierResponse()
        .of(r)
        .outlier(r.getMedian() < range[0] || r.getMedian() > range[1]))
        .collect(Collectors.toList());
  }
  
  Double[] interquartileRange(List<MedianEntity> records) {
    List<Double> values = records.stream().map(r -> r.getMedian()).collect(Collectors.toList());
    Collections.sort(values);
    double q1 = Quantiles.percentiles().index(25).compute(values);
    double q3 = Quantiles.percentiles().index(75).compute(values);
    return new Double[] {q1 - 1.5 * (q3 - q1), q3 + 1.5 * (q3 - q1)};//[low,high]
  }
}

class OutlierResponse extends MedianEntity {
  
  private boolean outlier;
  
  OutlierResponse of(MedianEntity original){
    this.setId(original.getId());
    this.setPublisher(original.getPublisher());
    this.setTime(original.getTime());
    this.setMedian(original.getMedian());
    return this;
  }
  
  OutlierResponse outlier(boolean outlier){
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

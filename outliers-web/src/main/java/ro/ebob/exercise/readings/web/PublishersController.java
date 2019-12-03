package ro.ebob.exercise.readings.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ro.ebob.exercise.readings.domain.OutlierResponse;
import ro.ebob.exercise.readings.data.domain.ReadingsEntity;
import ro.ebob.exercise.readings.data.service.ReadingsDataService;
import ro.ebob.exercise.readings.service.OutlierService;

@RestController
@RequestMapping("/publishers")
public class PublishersController {

  private ReadingsDataService dataService;
  private OutlierService outlierService;

  public PublishersController(ReadingsDataService dataService, OutlierService outlierService) {
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
  public List<ReadingsEntity> retrieveRecords(@RequestParam(required=false, defaultValue="10") Integer limit) {
    return dataService.last(limit);
  }
}

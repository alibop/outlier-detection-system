package ro.ebob.exercise.readings.consumer.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ro.ebob.exercise.readings.consumer.domain.MedianEntity;
import ro.ebob.exercise.readings.consumer.repository.MedianRepository;

@Service
public class MedianDataServiceImpl implements MedianDataService {

  private MedianRepository repository;

  public MedianDataServiceImpl(MedianRepository repository) {
    this.repository = repository;
  }
  
  public List<MedianEntity> last(int n) {
    return repository.findAll(PageRequest.of(0, n,  Sort.Direction.DESC, "id")).getContent();
  }

  public MedianEntity get(Long id) {
    return repository.getOne(id);
  }
  
  public MedianEntity save(MedianEntity entity) {
    return repository.save(entity);
  }

  @Override
  public List<MedianEntity> last(String publisher, int limit) {
    return repository.findAll(MedianRepository.publisherEquals(publisher), PageRequest.of(0, limit,  Sort.Direction.DESC, "id")).getContent();
  }
  

}

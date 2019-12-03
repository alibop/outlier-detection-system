package ro.ebob.exercise.readings.data.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ro.ebob.exercise.readings.data.domain.ReadingsEntity;
import ro.ebob.exercise.readings.data.repository.ReadingsRepository;

@Service
public class ReadingsDataServiceImpl implements ReadingsDataService {

  private ReadingsRepository repository;

  public ReadingsDataServiceImpl(ReadingsRepository repository) {
    this.repository = repository;
  }
  
  public List<ReadingsEntity> last(int n) {
    return repository.findAll(PageRequest.of(0, n,  Sort.Direction.DESC, "id")).getContent();
  }

  public ReadingsEntity get(Long id) {
    return repository.getOne(id);
  }
  
  public ReadingsEntity save(ReadingsEntity entity) {
    return repository.save(entity);
  }

  @Override
  public List<ReadingsEntity> last(String publisher, int limit) {
    return repository.findAll(ReadingsRepository.publisherEquals(publisher), PageRequest.of(0, limit,  Sort.Direction.DESC, "id")).getContent();
  }
}

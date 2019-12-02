package ro.ebob.exercise.readings.consumer.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ro.ebob.exercise.readings.consumer.domain.MedianEntity;

@Repository
public interface MedianRepository extends JpaRepository<MedianEntity, Long>, JpaSpecificationExecutor<MedianEntity> {

  public static Specification<MedianEntity> publisherEquals(String publisher) {
    return (root, query, cb) -> cb.equal(root.get("publisher"), publisher);
  }
}

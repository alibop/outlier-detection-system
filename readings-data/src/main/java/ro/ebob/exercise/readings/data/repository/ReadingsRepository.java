package ro.ebob.exercise.readings.data.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ro.ebob.exercise.readings.data.domain.ReadingsEntity;

@Repository
public interface ReadingsRepository extends JpaRepository<ReadingsEntity, Long>, JpaSpecificationExecutor<ReadingsEntity> {

  public static Specification<ReadingsEntity> publisherEquals(String publisher) {
    return (root, query, cb) -> cb.equal(root.get("publisher"), publisher);
  }
}

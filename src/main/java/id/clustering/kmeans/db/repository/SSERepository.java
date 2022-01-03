package id.clustering.kmeans.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import id.clustering.kmeans.db.model.SSEModel;

@Repository
public interface SSERepository extends MongoRepository<SSEModel, Long>{

}

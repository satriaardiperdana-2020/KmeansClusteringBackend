package id.clustering.kmeans.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import id.clustering.kmeans.db.model.HasilCluster;

@Repository
public interface HasilClusterRepository extends MongoRepository<HasilCluster, Long>{

}

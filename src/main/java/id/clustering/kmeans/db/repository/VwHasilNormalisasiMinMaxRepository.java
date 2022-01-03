package id.clustering.kmeans.db.repository;

import java.util.LinkedList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import id.clustering.kmeans.db.model.VwHasilNormalisasiMinMaxModel;

@Repository
public interface VwHasilNormalisasiMinMaxRepository extends JpaRepository<VwHasilNormalisasiMinMaxModel, String>{

	@Query(value="select * from vw_hasil_normalisasi_min_max_latihan limit 10", nativeQuery = true)
	LinkedList<VwHasilNormalisasiMinMaxModel> fetchData(); 

}

package id.clustering.kmeans.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "hasil_cluster")
public class HasilCluster {
	@Id
    private String id;
	
	private Integer cluster;
	private Double frekuensi;
	private Double total;
	

}

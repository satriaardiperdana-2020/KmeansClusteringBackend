package id.clustering.kmeans.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "sse")
public class SSEModel {
	@Id
    private String id;
	private int cluster;
	private Double nilaiSSE;
	private Double SelisihSSE;


}

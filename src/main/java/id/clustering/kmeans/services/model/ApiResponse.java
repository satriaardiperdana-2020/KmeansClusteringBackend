package id.clustering.kmeans.services.model;

import lombok.Data;

@Data
public class ApiResponse {
	 public int statusCode = 0;
	 public String message = "";
	 public String status = "";
	 public Object data;
}

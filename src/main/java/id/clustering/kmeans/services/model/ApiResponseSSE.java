package id.clustering.kmeans.services.model;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class ApiResponseSSE {
	 public int statusCode = 0;
	 public String message = "";
	 public String status = "";
	 public List<SSEResponse> data;
}

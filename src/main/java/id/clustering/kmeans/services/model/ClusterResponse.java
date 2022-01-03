package id.clustering.kmeans.services.model;

import java.util.LinkedList;

import lombok.Data;

@Data
public class ClusterResponse {
	public LinkedList<Double> clusters1 = new LinkedList<>();
	public LinkedList<Double> clusters2 = new LinkedList<>();

}

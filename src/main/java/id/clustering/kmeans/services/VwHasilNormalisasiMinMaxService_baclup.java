package id.clustering.kmeans.services;

import java.awt.RenderingHints.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.clustering.kmeans.db.repository.VwHasilNormalisasiMinMaxRepository;
import id.clustering.kmeans.services.model.ClusterResponse;
import id.clustering.kmeans.db.model.VwHasilNormalisasiMinMaxModel;

@Service
public class VwHasilNormalisasiMinMaxService_baclup {

	private Logger logService = LoggerFactory.getLogger(this.getClass());

	@Autowired
	VwHasilNormalisasiMinMaxRepository vwHasilNormalisasiMinMaxRepository;

	// LinkedList<VwHasilNormalisasiMinMaxModel> data = new
	// LinkedList<VwHasilNormalisasiMinMaxModel>();

	LinkedHashMap<String, Double> record = new LinkedHashMap<>();

	Double centroidNormalisasiFrekuensi1 = 0.272727280855179;
	Double centroidNormalisasiTotal1 = 1.0;
	Double centroidNormalisasiFrekuensi2 = 0.181818187236786;
	Double centroidNormalisasiTotal2 = 0.0;
	Double d1;
	Double d2;
	Double newCentroidFrekuensi1;
	Double newCentroidFrekuensi2;
	Double newCentroidTotal1;
	Double newCentroidTotal2;
	Double sumC1 = 0.0, sumC1_1 = 0.0, sumC2 = 0.0, sumC2_1 = 0.0;
	LinkedList<Double> clusters1 = new LinkedList<>();
	LinkedList<Double> clusters2 = new LinkedList<>();
	//LinkedHashMap<Integer, Double> clusters1 = new LinkedHashMap<>();
	//LinkedHashMap<Integer, Double> clusters2 = new LinkedHashMap<>();
	LinkedHashMap<Integer, Double> newClusters1 = new LinkedHashMap<>();
	LinkedHashMap<Integer, Double> newClusters2 = new LinkedHashMap<>();

	LinkedHashMap<Integer, Double> dataFrekuensi4RecomputeCentroidC1 = new LinkedHashMap<>();
	LinkedHashMap<Integer, Double> dataFrekuensi4RecomputeCentroidC2 = new LinkedHashMap<>();
	LinkedHashMap<Integer, Double> dataTotal4RecomputeCentroidC1 = new LinkedHashMap<>();
	LinkedHashMap<Integer, Double> dataTotal4RecomputeCentroidC2 = new LinkedHashMap<>();

	// UNtuk getAllData
	static final Double PRECISION = 0.0;

	// LinkedList<String> centroids = new LinkedList<>();
	LinkedList<VwHasilNormalisasiMinMaxModel> records = new LinkedList<VwHasilNormalisasiMinMaxModel>();

	private static final Random random = new Random();
	//private final LinkedList<Integer> indicesOfCentroids = new LinkedList<>();
	
	


	// end getAllData

	public ClusterResponse getAllData(Integer K) {
		ClusterResponse clusterResponse = new ClusterResponse();
		records.clear();
		LinkedList<VwHasilNormalisasiMinMaxModel> getDataSet = vwHasilNormalisasiMinMaxRepository.fetchData();
		// LinkedList<VwHasilNormalisasiMinMaxModel> dataset = new
		// LinkedList<VwHasilNormalisasiMinMaxModel>();
		for (VwHasilNormalisasiMinMaxModel dataTemporary : getDataSet) {
			records.add(dataTemporary);

			//System.out.println("liat tok===" + dataTemporary);
		}
		clusterResponse = kmeans(records, K);
		System.out.println("kluarin clusterResponseclusterResponseclusterResponse ii"+clusterResponse);
		return clusterResponse;

	}

	public ClusterResponse kmeans(LinkedList<VwHasilNormalisasiMinMaxModel> records, Integer K) {
		// System.out.println("2. Lihat dataset = "+records);
		ClusterResponse clusterResponse = new ClusterResponse();
		//System.out.println("2. Lihat K? = " + K);
		LinkedList<VwHasilNormalisasiMinMaxModel> centroids = kmeanspp(records, K);
		//System.out.println("35. lll" + centroids);// sama kaya 34 ok
		euclideandistance(records, centroids);
		//System.out.println("Lihat euclideandistance= "+euclideandistance);
		clusterResponse.setClusters1(clusters1);
		clusterResponse.setClusters2(clusters2);
		logService.info("Lihat cluster respon di kmeans : "+clusterResponse);
		return clusterResponse;
		
		// centroids.add(randomFromDataSet(data));//ini random

	}

	private ClusterResponse euclideandistance(LinkedList<VwHasilNormalisasiMinMaxModel> records, LinkedList<VwHasilNormalisasiMinMaxModel> centroids) {
		ClusterResponse clusterResponse = new ClusterResponse();
		LinkedList<Double> getListD1 = new LinkedList<>();
		clusters1.clear();
		clusters2.clear();
		


		//ArrayList<LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double>> lisMaptD1 = new ArrayList<LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double>>();
		//ArrayList<LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double>> lisMaptD2 = new ArrayList<LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double>>();
		ArrayList<LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double>> lisMaptD1 = new ArrayList<LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double>>();
		ArrayList<LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double>> lisMaptD2 = new ArrayList<LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double>>();
		LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double> mapD1 = new LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double>();
		LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double> mapD2 = new LinkedHashMap<VwHasilNormalisasiMinMaxModel, Double>();
		LinkedList<Double> mathmin = new LinkedList<>();

		int x = 0;
		for (int i = 0; i < centroids.size(); i++) {
			
			logService.info("Cek centroid pake for=   . " + centroids.get(i) + "ke: " + (i + 1));
			for (int j = 0; j < records.size(); j++) {
				 //d1 = Math.sqrt((Math.pow((records.get(j).getNormalisasiFrekuensi() - centroids.get(i).getNormalisasiFrekuensi()), 2)) + (Math.pow((records.get(j).getNormalisasiTotal() - centroids.get(i).getNormalisasiTotal()),2)));
				//System.out.println("NOnton d1=" + distance1);

				//mapD1.put(records.get(j), distance1);
				if(x==0) {
					d1 = Math.sqrt((Math.pow((records.get(j).getNormalisasiFrekuensi() - centroids.get(i).getNormalisasiFrekuensi()), 2)) + (Math.pow((records.get(j).getNormalisasiTotal() - centroids.get(i).getNormalisasiTotal()),2)));
					//clusters1.add(d1);
					mapD1.put(records.get(j), d1);
					//System.out.println("satu. "+d1);
				}
				if (x==1){
					
					d2 = Math.sqrt((Math.pow((records.get(j).getNormalisasiFrekuensi() - centroids.get(i).getNormalisasiFrekuensi()), 2)) + (Math.pow((records.get(j).getNormalisasiTotal() - centroids.get(i).getNormalisasiTotal()),2)));
					//clusters2.add(d2);
					//System.out.println("Dua. "+d2);
					mapD2.put(records.get(j), d2);
				}
			/*	if(d1 !=null && d2!=null && Math.min(d1, d2)== d1){
					clusters1.add(d1);
					logService.info("Liat di iterasi :"+clusters1);
					
					//mapD1.put(records.get(j), d1);
				}else if(d1 !=null && d2!=null && Math.min(d1, d2)== d2){
					clusters2.add(d2);
					logService.info("Liat di iterasi 2:"+clusters2);
					//mapD2.put(records.get(j), d2);
				}*/
			
				
			}
			
			//if (Math.min(mapD1.values(), mapD2.values())== mapD1.values() ) {
			//	
			//}
			/*if (Math.min(d1, d2) == d1) {// hasil cluster c11c/ yg dikolom d1
			// clusters1.add(dataTemp.getNormalisasiFrekuensi());
			clusters1.put(dataTemp.getTbmmId(), d1);
			dataFrekuensi4RecomputeCentroidC1.put(dataTemp.getTbmmId(), dataTemp.getNormalisasiFrekuensi());// 4RecomputeCentroid
			dataTotal4RecomputeCentroidC1.put(dataTemp.getTbmmId(), dataTemp.getNormalisasiTotal());// 4RecomputeCentroid
		} else if (Math.min(d1, d2) == d2) {//// hasil cluster c2/ yg dikolom d2
			// clusters2.add(dataTemp.getNormalisasiTotal());
			clusters2.put(dataTemp.getTbmmId(), d2);
			dataFrekuensi4RecomputeCentroidC2.put(dataTemp.getTbmmId(), dataTemp.getNormalisasiFrekuensi());// 4RecomputeCentroid
			dataTotal4RecomputeCentroidC2.put(dataTemp.getTbmmId(), dataTemp.getNormalisasiTotal());// 4RecomputeCentroid
		}
		}
		*/
			x++;
		}
		
		

		 for (VwHasilNormalisasiMinMaxModel key : mapD1.keySet()) {
	            Double v1 = mapD1.get(key);
	            Double v2 = mapD2.get(key);
	            
	            if(v1 !=null && v2!=null && Math.min(v1, v2)== v1){
					clusters1.add(v1);
	            }
	            if(v1 !=null && v2!=null && Math.min(v1, v2)== v2){
					clusters2.add(v2);
	            }
	            
	            
	            
	            
		 }
		 
			logService.info("Liat di iterasi :"+clusters1);
			logService.info("Liat di iterasi 2 :"+clusters2);
			clusterResponse.setClusters1(clusters1);
			clusterResponse.setClusters2(clusters2);
			System.out.println("nonton respon di akhir : "+clusterResponse);
			return clusterResponse;
			
		
		
		

	}

	public LinkedList<VwHasilNormalisasiMinMaxModel> kmeanspp(LinkedList<VwHasilNormalisasiMinMaxModel> data,
			Integer K) {
		LinkedList<VwHasilNormalisasiMinMaxModel> centroids = new LinkedList<>();
		centroids.clear();
		System.out.println("data dalam centroid===" + data);
		centroids.addAll(randomFromDataSet(data, K));
		System.out.println("34. Keluarin centroid = ...." + centroids);
		/*
		 * for(int i=1; i<K; i++){ centroids.add(calculateWeighedCentroid(data));
		 * System.out.println("35. data dalam centroid for ===" + centroids);
		 * 
		 * }
		 */
		// System.out.println("6. Cek return centroid====="+centroids);
		return centroids;
	}

	public LinkedList<VwHasilNormalisasiMinMaxModel> randomFromDataSet(LinkedList<VwHasilNormalisasiMinMaxModel> data,
			Integer K) {
		int index = 0;
		LinkedList<VwHasilNormalisasiMinMaxModel> listCentroid = new LinkedList<>();
		for (int i = 0; i < K; i++) {
			index = random.nextInt(data.size());
			listCentroid.add(data.get(index));
			// System.out.println("33.Lihat Index===" + records.get(index).getClass());
			System.out.println("33.1.Lihat Index===" + data.get(index)); // return
			// records.get(index).getClass();

		}
		return listCentroid;
	}



	private Double euclideanDistance(VwHasilNormalisasiMinMaxModel a, VwHasilNormalisasiMinMaxModel b) {
		if (!a.getTbmmId().equals(b.getTbmmId())) {
			return Double.POSITIVE_INFINITY;
		}

		double sum = 0.0;
		System.out.println("a...." + a.getNormalisasiFrekuensi());
		System.out.println("b...." + b);

		return Math.sqrt(sum);
	}

	public void getAllDataMInMax(Integer K) {
		List<VwHasilNormalisasiMinMaxModel> getData = vwHasilNormalisasiMinMaxRepository.fetchData();
		for (VwHasilNormalisasiMinMaxModel dataTemp : getData) {
			// Euclidience Distance
			if (K == 2) {
				d1 = Math.sqrt((Math.pow((dataTemp.getNormalisasiFrekuensi() - centroidNormalisasiFrekuensi1), 2))
						+ (Math.pow(dataTemp.getNormalisasiTotal() - centroidNormalisasiTotal1, 2)));
				d2 = Math.sqrt((Math.pow((dataTemp.getNormalisasiFrekuensi() - centroidNormalisasiFrekuensi2), 2))
						+ (Math.pow(dataTemp.getNormalisasiTotal() - centroidNormalisasiTotal2, 2)));
				// Menempatkan anggota Cluster
				if (Math.min(d1, d2) == d1) {// hasil cluster c11c/ yg dikolom d1
					// clusters1.add(dataTemp.getNormalisasiFrekuensi());
					//clusters1.put(dataTemp.getTbmmId(), d1);
					dataFrekuensi4RecomputeCentroidC1.put(dataTemp.getTbmmId(), dataTemp.getNormalisasiFrekuensi());// 4RecomputeCentroid
					dataTotal4RecomputeCentroidC1.put(dataTemp.getTbmmId(), dataTemp.getNormalisasiTotal());// 4RecomputeCentroid
				} else if (Math.min(d1, d2) == d2) {//// hasil cluster c2/ yg dikolom d2
					// clusters2.add(dataTemp.getNormalisasiTotal());
					//clusters2.put(dataTemp.getTbmmId(), d2);
					dataFrekuensi4RecomputeCentroidC2.put(dataTemp.getTbmmId(), dataTemp.getNormalisasiFrekuensi());// 4RecomputeCentroid
					dataTotal4RecomputeCentroidC2.put(dataTemp.getTbmmId(), dataTemp.getNormalisasiTotal());// 4RecomputeCentroid
				}
			}
			logService.info("=====LIHAT DATA======= d1111==" + d1);
			logService.info("=====LIHAT DATA 2======= d2222==" + d2);
			// logService.info("=====LIHAT DATA 3======= d33=="+d3);
		} //
		logService.info("lihat c1" + clusters1);
		logService
				.info("lihat dataFrekuensi4RecomputeCentroid 4 recompute centroid" + dataFrekuensi4RecomputeCentroidC1);
		logService.info("lihat c2" + clusters2);
		logService.info("lihat dataTotal4RecomputeCentroid 4 recompute centroid" + dataTotal4RecomputeCentroidC2);

		// Recompute centroid
		for (Map.Entry<Integer, Double> setC1 : dataFrekuensi4RecomputeCentroidC1.entrySet()) {
			logService.info("C1 ===" + setC1.getValue());
			sumC1 += setC1.getValue();//
		}
		for (Map.Entry<Integer, Double> setC1_2 : dataFrekuensi4RecomputeCentroidC2.entrySet()) {
			logService.info("C1_2 ===" + setC1_2.getValue());
			sumC1_1 += setC1_2.getValue();//
		}

		for (Map.Entry<Integer, Double> setC2 : dataTotal4RecomputeCentroidC1.entrySet()) {
			logService.info("C2 ===" + setC2.getValue());
			sumC2 += setC2.getValue();
		}

		for (Map.Entry<Integer, Double> setC2_1 : dataTotal4RecomputeCentroidC2.entrySet()) {
			logService.info("C2_2 ===" + setC2_1.getValue());
			sumC2_1 += setC2_1.getValue();
		}

		// Hitung ulang centroid c1 frekuensi, c2 total
		newCentroidFrekuensi1 = sumC1.doubleValue() / dataFrekuensi4RecomputeCentroidC1.size();
		newCentroidFrekuensi2 = sumC1_1.doubleValue() / dataFrekuensi4RecomputeCentroidC2.size();
		newCentroidTotal1 = sumC2.doubleValue() / dataTotal4RecomputeCentroidC1.size();
		newCentroidTotal2 = sumC2_1.doubleValue() / dataTotal4RecomputeCentroidC2.size();

		logService.info("=====LIHAT DATA======= sum==" + sumC1.doubleValue());
		logService.info("=====LIHAT DATA======= sum==" + sumC1_1.doubleValue());
		logService.info("=====LIHAT DATA newCentroidFrekuensi1======= ==" + newCentroidFrekuensi1);
		logService.info("=====LIHAT DATA newCentroidFrekuensi12======= ==" + newCentroidFrekuensi2);

		logService.info("=====LIHAT DATA======= sum2==" + sumC2.doubleValue());
		logService.info("=====LIHAT DATA======= sum2==" + sumC2_1.doubleValue());
		logService.info("=====LIHAT DATA newCentroid 2======= newCentroidTotal1==" + newCentroidTotal1);
		logService.info("=====LIHAT DATA newCentroid 22======= newCentroidTotal2==" + newCentroidTotal2);
		// int clusterNumber = 2;//k = cluster

		// Looping iterasi ecluidence lagi
		//if (!clusters1.containsKey(newClusters1)) {

		//}

	}

}

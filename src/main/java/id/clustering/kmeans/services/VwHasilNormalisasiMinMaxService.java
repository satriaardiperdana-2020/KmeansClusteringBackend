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
import java.util.Random;import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.clustering.kmeans.db.repository.HasilClusterRepository;
import id.clustering.kmeans.db.repository.SSERepository;
import id.clustering.kmeans.db.repository.VwHasilNormalisasiMinMaxRepository;
import id.clustering.kmeans.db.model.HasilCluster;
import id.clustering.kmeans.db.model.Record;
import id.clustering.kmeans.db.model.SSEModel;
import id.clustering.kmeans.services.model.ClusterResponse;
import id.clustering.kmeans.services.model.SSEResponse;
import id.clustering.kmeans.db.model.VwHasilNormalisasiMinMaxModel;

@Service
public class VwHasilNormalisasiMinMaxService {
	private Logger logService = LoggerFactory.getLogger(this.getClass());

	@Autowired
	VwHasilNormalisasiMinMaxRepository vwHasilNormalisasiMinMaxRepository;
	
	@Autowired
	SSERepository sseRepository;
	
	@Autowired 
	HasilClusterRepository hasilClusterRepository;

	//LinkedHashMap<String, Double> record = new LinkedHashMap<>();

	//Double d1;
	//Double d2;
	//LinkedList<Double> clusters1 = new LinkedList<>();
	//LinkedList<Double> clusters2 = new LinkedList<>();


	// UNtuk getAllData
	static final Double PRECISION = 0.0;

	private final LinkedList<String> attrNames = new LinkedList<>();
    private final static LinkedList<Record> records = new LinkedList<>();
    private final static LinkedList<Integer> indicesOfCentroids = new LinkedList<>();
    private final HashMap<String, Double> minimums = new HashMap<>();
    private final HashMap<String, Double> maximums = new HashMap<>();
    private static final Random random = new Random();
     //endternew
	public ClusterResponse getAllData(Integer K) {
		ClusterResponse clusterResponse = new ClusterResponse();
		records.clear();
		attrNames.clear();
		indicesOfCentroids.clear();
		maximums.clear();
		minimums.clear();
		
		LinkedList<VwHasilNormalisasiMinMaxModel> getDataSet = vwHasilNormalisasiMinMaxRepository.fetchData();
		System.out.println("1. JUmlah Record Dari db = "+getDataSet.size());
		attrNames.push("normalisasi_total");
	    attrNames.push("normalisasi_frekuensi");
	    attrNames.push("Class");
	   // records.clear();
	    
		for (VwHasilNormalisasiMinMaxModel dataSet : getDataSet) {
			 HashMap<String, Double> record = new HashMap<>();
			 int id = dataSet.getTbmmId();
	         Double frekuensi= dataSet.getNormalisasiFrekuensi();
	         Double total = dataSet.getNormalisasiTotal();
			//records.add(dataTemporary);
	         Double idCast = (double) id;
	         record.put("Class", idCast);
	         record.put("normalisasi_frekuensi", frekuensi);
	         record.put("normalisasi_total", total);
	        
	         updateMin("Class", idCast);
	         updateMin("normalisasi_frekuensi", frekuensi);
	         updateMin("normalisasi_total", total);

	         updateMax("Class", idCast);
	         updateMax("normalisasi_frekuensi", frekuensi);
	         updateMax("normalisasi_total", total);
	         //System.out.println("xxx = "+record.get(record));
	         records.add(new Record(record));
	         

		}
		for (int i = 0; i < records.size(); i++) {
	    	  
	          // Print all elements of List
	          //System.out.println("coba kluar recod : "+records.get(i).getRecord());
	    }
		kmeans(records, K);
		
		return clusterResponse;

	}

	public void removeAttr(String attrName, LinkedList<Record> data){
        if(attrNames.contains(attrName)){
            attrNames.remove(attrName);

            for(Record record : data){
                record.getRecord().remove(attrName);
            }

            minimums.remove(attrName);

            maximums.remove(attrName);
        }

    }
	private void updateMax(String name, Double val){
        if(maximums.containsKey(name)){
            if(val > maximums.get(name)){
                maximums.put(name, val);
            }
        } else{
            maximums.put(name, val);
        }
    }

	private void updateMin(String name, Double val){
        if(minimums.containsKey(name)){
            if(val < minimums.get(name)){
                minimums.put(name, val);
            }
        } else{
            minimums.put(name, val);
        }

    }
	public ClusterResponse kmeans(LinkedList<Record> data, Integer K) {
		// System.out.println("2. Lihat dataset = "+records);
		ClusterResponse clusterResponse = new ClusterResponse();
		removeAttr("Class", data);
		//LinkedList<VwHasilNormalisasiMinMaxModel> centroids = kmeanspp(records, K);
		LinkedList<HashMap<String,Double>> centroids = kmeanspp(data, K);
		System.out.println("1. Cek isi centroids==="+centroids);
		
		Double SSE = Double.MAX_VALUE;
		
		while (true) {
           for(Record record : data){
           	//System.out.println("6. liat get data==="+record.getRecord());//keluarin data yg di excel
               Double minDist = Double.MAX_VALUE;
               //System.out.println("7. Keluarin mindist==="+minDist);
               // find the centroid at a minimum distance from it and add the record to its cluster
               for(int i=0; i<centroids.size(); i++){
               	//System.out.println("8. get centroid i"+centroids.get(i));
                   Double dist = euclideanDistance(centroids.get(i), record.getRecord());
                  // System.out.println("9. Liat ecludience ==="+dist);
                   if(dist<minDist){
                       minDist = dist;
                       record.setClusterNo(i);
                       System.out.println("10. liat hasil"+record.getRecord());//Hasilnya sama seperti di excel/data source cuma duplicate dan yg puluhan satuan
                       System.out.println("11. liat hasil cluster "+record.getClusterNo());
                       //SAMPE SINI
                   }
               }

           }

           // recompute centroids   ;] to new cluster assignments
           centroids = recomputeCentroids(data, K);
           System.out.println("12. LIhat recompute centroid"+centroids);
           

           // exit condition, SSE changed less than PRECISION parameter
           
           Double newSSE = calculateTotalSSE(centroids, data);
           System.out.println("13 before . LIhat NEWSSE===="+newSSE);
          // Double newSSE = 0.0;
           if(SSE-newSSE <= PRECISION){
               break;
           }
           
           SSE = newSSE;
           
           
           System.out.println("13. LIhat SSE===="+SSE);
           
           
       }
		SSEModel sseModel = new SSEModel();
        sseModel.setCluster(K);
        sseModel.setNilaiSSE(SSE);
        
        sseRepository.save(sseModel);
		insertHasilCluster(data); 
		//clusterResponse.setClusters1(clusters1);
		//clusterResponse.setClusters2(clusters2);
		//logService.info("Lihat cluster respon di kmeans : "+clusterResponse);
		return clusterResponse;
		
		// centroids.add(randomFromDataSet(data));//ini random

	}

	public Double calculateTotalSSE(LinkedList<HashMap<String,Double>> centroids, LinkedList<Record> data){
		System.out.println("20. LIhat centroid SEE = "+centroids);
        Double SSE = 0.0;
        for(int i=0; i<centroids.size(); i++) {
            SSE += calculateClusterSSE(centroids.get(i), i, data);
            //System.out.println("13. SSE ====="+i+"." +SSE);
        }
        return SSE;
    }

	 public Double calculateClusterSSE(HashMap<String, Double> centroid, int clusterNo, LinkedList<Record> data){
		 System.out.println("21. records.size SEE = "+data.size());
	        double SSE = 0.0;
	        for(int i=0; i<data.size(); i++){
	            if(data.get(i).clusterNo == clusterNo){
	                SSE += Math.pow(euclideanDistance(centroid, data.get(i).getRecord()), 2);
	            }
	        }
	        return SSE;
	 }

	private void insertHasilCluster(LinkedList<Record> data) {
		// TODO Auto-generated method stub
		for(Record record : data) {
			 Double normalisasi_frekuensi_db = record.getRecord().get("normalisasi_frekuensi");
			 Double normalisasi_total_db = record.getRecord().get("normalisasi_total");
             Integer clusterNumber = record.clusterNo;
             //Double parseDouble = (double)clusterNumber; 
             System.out.println("Normalisasi Frekuensi = "+normalisasi_frekuensi_db+ "Total = "+normalisasi_total_db+ "Cluster = "+clusterNumber);
             HasilCluster hasilCluster = new HasilCluster();
             hasilCluster.setFrekuensi(normalisasi_frekuensi_db);
             hasilCluster.setTotal(normalisasi_total_db);
             hasilCluster.setCluster(clusterNumber);
             hasilClusterRepository.save(hasilCluster);
		}
		
	}

	public LinkedList<HashMap<String,Double>> recomputeCentroids(LinkedList<Record> data, int K){
        LinkedList<HashMap<String,Double>> centroids = new LinkedList<>();
        for(int i=0; i<K; i++){
            centroids.add(calculateCentroid(i, data));
            //System.out.println("13. centro = "+centroids);
        }
        
        return centroids;
    }

	public HashMap<String, Double> calculateCentroid(int clusterNo, LinkedList<Record> data){
        HashMap<String, Double> centroid = new HashMap<>();
        System.out.println("14 data lih = "+data.size());
        LinkedList<Integer> recsInCluster = new LinkedList<>();
        for(int i=0; i<data.size(); i++){
            Record record = data.get(i);
            if(record.clusterNo == clusterNo){
                recsInCluster.add(i);
                //System.out.println("15. Lihat recsInCluster = "+recsInCluster);
            }
        }

        for(String name : attrNames){
            centroid.put(name, meanOfAttr(name, recsInCluster, data));
        }
        return centroid;
    }

	public Double meanOfAttr(String attrName, LinkedList<Integer> indices, LinkedList<Record> data){
        Double sum = 0.0;
        for(int i : indices){
            if(i<data.size()){
                sum += data.get(i).getRecord().get(attrName);
            }
        }
        System.out.println("16. Lihat sum / indices.size() = "+sum / indices.size());
        return sum / indices.size();
    }

	 public static LinkedList<HashMap<String, Double>> kmeanspp(LinkedList<Record> data, Integer K) {
		LinkedList<HashMap<String,Double>> centroids = new LinkedList<>();
		//centroids.clear();
		//System.out.println("data dalam centroid===" + data);
		centroids.add(randomFromDataSet());
		System.out.println("34. Keluarin centroid = ...." + centroids);
		for(int i=1; i<K; i++){
            centroids.add(calculateWeighedCentroid());
            System.out.println("5. Cek Centroid add dalam for"+centroids);
        }
		return centroids;
	}
	
	 public static HashMap<String,Double> calculateWeighedCentroid(){
	        double sum = 0.0;
	        //System.out.println("101. cek isi records "+records);
	        for(int i=0; i<records.size(); i++){
	            if(!indicesOfCentroids.contains(i)){
	                double minDist = Double.MAX_VALUE;
	                for(int ind : indicesOfCentroids){
	                    double dist = euclideanDistance(records.get(i).getRecord(), records.get(ind).getRecord());
	                    if(dist<minDist)
	                        minDist = dist;
	                }
	                if(indicesOfCentroids.isEmpty())
	                    sum = 0.0;
	                sum += minDist;
	            }
	        }

	        double threshold = sum * random.nextDouble();

	        for(int i=0; i<records.size(); i++){
	            if(!indicesOfCentroids.contains(i)){
	                double minDist = Double.MAX_VALUE;
	                for(int ind : indicesOfCentroids){
	                    double dist = euclideanDistance(records.get(i).getRecord(), records.get(ind).getRecord());
	                    if(dist<minDist)
	                        minDist = dist;
	                }
	                sum += minDist;

	                if(sum > threshold){
	                    indicesOfCentroids.add(i);
	                    return records.get(i).getRecord();
	                }
	            }
	        }

	        return new HashMap<>();
	    }
	
	public static HashMap<String, Double> randomFromDataSet(){
        int index = random.nextInt(records.size());
        System.out.println("Lihat index randomFromDataSet = "+index);
        return records.get(index).getRecord();
    }

	private static Double euclideanDistance(HashMap<String, Double> a, HashMap<String, Double> b) {
		if(!a.keySet().equals(b.keySet())){
            return Double.POSITIVE_INFINITY;
        }

        double sum = 0.0;

        for(String attrName : a.keySet()){
            sum += Math.pow(a.get(attrName) - b.get(attrName), 2);
        }
        System.out.println("14. Math.sqrt(sum)=="+Math.sqrt(sum));
		return Math.sqrt(sum);
	}

	public List<SSEResponse> getAllDataSSE() {
		
		List<SSEResponse> sseResponseList = new ArrayList<SSEResponse>();
		List<Integer> clusterNoList = new ArrayList<>(); 
		List<Double> nilaiSSE = new LinkedList<>(); 
		Iterable<SSEModel> getAllDataSSE = sseRepository.findAll();
		for (SSEModel dataSSE : getAllDataSSE) {
			SSEResponse sseResponse = new SSEResponse();
			Integer clusterNo = dataSSE.getCluster();
			sseResponse.setCluster(clusterNo);
			sseResponse.setNilaiSSE(dataSSE.getNilaiSSE());
			//sseResponseList.add(sseResponse);
			//clusterNoList.add(clusterNo);
			//Double nilaiSSE = dataSSE.getNilaiSSE();
			//clusterNo.add(dataSSE.getCluster());
			//nilaiSSE.add(dataSSE.getNilaiSSE());
			System.out.println("Hsil get All SSE: "+sseResponse);
			//System.out.println("clusterNoList: "+clusterNoList);
			sseResponseList.add(sseResponse);
			System.out.println("clusterNoList: "+sseResponseList);
		}
		
		//sseResponse.setCluster(clusterNo);
		//sseResponse.set(0, null)
		return sseResponseList;
		
	}

	

}

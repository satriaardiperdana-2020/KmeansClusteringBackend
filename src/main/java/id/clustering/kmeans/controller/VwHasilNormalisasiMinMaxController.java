package id.clustering.kmeans.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import id.clustering.kmeans.db.model.*;
import id.clustering.kmeans.services.*;
import id.clustering.kmeans.services.model.ApiResponse;
import id.clustering.kmeans.services.model.ApiResponseSSE;
import id.clustering.kmeans.services.model.ClusterResponse;
import id.clustering.kmeans.services.model.ClusteringModel;
import id.clustering.kmeans.services.model.SSEResponse;

@RestController


public class VwHasilNormalisasiMinMaxController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private VwHasilNormalisasiMinMaxService vwHasilNormalisasiMinMaxService;
	//
	//@GetMapping("/loadDataMinmax")
	@PostMapping("/loadDataMinmax")
	public HttpEntity<?> getAllDataMinMax(@RequestBody ClusteringModel request){
		ApiResponse response = new ApiResponse();
		HttpStatus status = HttpStatus.OK;
		//Clust
		ClusterResponse clusterRes = new ClusterResponse();
		try {
			//Integer K = 2;//Diganti jadi request input
			Integer K = request.getK();
			log.info("1. Cek nilai K= "+K);
			log.info("2. Cek nilai K req= "+request.getK());
			
			//clusterRes = vwHasilNormalisasiMinMaxService.getAllData(K);
			vwHasilNormalisasiMinMaxService.getAllData(K);
			//response.setData(clusterRes);
			//vwHasilNormalisasiMinMaxService.kmeans(records, K);
			//kmeans(data, K);
			System.out.println("11111 "+clusterRes);
			log.info("cek hasil ke service");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(response, status);
	}
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping("/hasilsse")
	public HttpEntity<?> getAllDataSSE(){
		ApiResponseSSE responseSSE = new ApiResponseSSE();
		HttpStatus status = HttpStatus.OK;
		//List<SSEResponse> sseResponse = new SSEResponse();
		List<SSEResponse> sseResponse = new ArrayList<SSEResponse>();
		
		sseResponse = vwHasilNormalisasiMinMaxService.getAllDataSSE();
		responseSSE.setData(sseResponse);
		return new ResponseEntity<>(sseResponse, status);
		
	}
	
	
	

}

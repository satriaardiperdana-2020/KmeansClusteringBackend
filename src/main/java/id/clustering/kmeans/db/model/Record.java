package id.clustering.kmeans.db.model;

import java.util.HashMap;

import lombok.Data;

@Data
public class Record{
    HashMap<String, Double> record;
    public Integer clusterNo;

    public Record(HashMap<String, Double> record){
        this.record = record;
    }

    public void setClusterNo(Integer clusterNo) {
        this.clusterNo = clusterNo;
    }

    public HashMap<String, Double> getRecord() {
        return record;
    }
}

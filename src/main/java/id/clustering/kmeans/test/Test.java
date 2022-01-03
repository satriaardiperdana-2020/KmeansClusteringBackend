package id.clustering.kmeans.test;

import java.sql.SQLException;  

public class Test {  
  public static void main(String[] args) throws SQLException {  
    Koneksi connection = new Koneksi();  
    connection.getKoneksi();  
  }  
}  
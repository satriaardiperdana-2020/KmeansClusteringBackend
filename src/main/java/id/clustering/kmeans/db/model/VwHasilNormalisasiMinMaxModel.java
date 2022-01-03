package id.clustering.kmeans.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;
//Ditutorial sama kaya class Record
@Data
@Entity
@Table(name="vw_hasil_normalisasi_min_max_latihan")
public class VwHasilNormalisasiMinMaxModel {
	
	/*
	 * @Column(name = "no_transaksi", nullable = false) private Integer noTransaksi;
	 */
	@Id
	@Column(name = "tbmm_id") 
	private Integer tbmmId;
	
	
	@Column(name = "normalisasi_frekuensi")
	private Double normalisasiFrekuensi;
	
	
	@Column(name = "normalisasi_total")
	private Double normalisasiTotal;
	
	/*
	 * @Column(name = "normalisasi_tipe_pembayaran", nullable = false) private
	 * Double normalisasiTipePembayaran;
	 * 
	 * @Column(name = "normalisasi_kota", nullable = false) private Double
	 * normalisasiKota;
	 */
	//@Column(name = "cluster_number")
	//private Integer clusterNumber;//cara India bisa dihapus kalau ngga dipake
}

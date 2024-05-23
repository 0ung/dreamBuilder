package codehows.dream.dreambulider.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "file_manage")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileManage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fileMng_id")
	private Long id;

	private Integer uploadSize;
	private Integer uploadNum;
	@Column(nullable = false, columnDefinition = "TEXT")
	private String docExtension;
	@Column(nullable = false, columnDefinition = "TEXT")
	private String imageExtension;
	@Column(nullable = false, columnDefinition = "TEXT")
	private String videoExtension;

	@Builder
	public FileManage(Integer uploadSize, Integer uploadNum, String docExtension, String imageExtension, String videoExtension) {
		this.uploadSize = uploadSize;
		this.uploadNum = uploadNum;
		this.docExtension = docExtension;
		this.imageExtension = imageExtension;
		this.videoExtension = videoExtension;
	}

	public void update(Integer uploadSize, Integer uploadNum, String docExtension, String imageExtension, String videoExtension) {
		this.uploadSize = uploadSize;
		this.uploadNum = uploadNum;
		this.docExtension = docExtension;
		this.imageExtension = imageExtension;
		this.videoExtension = videoExtension;
	}


}

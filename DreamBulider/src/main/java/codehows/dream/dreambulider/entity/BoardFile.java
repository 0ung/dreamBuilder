package codehows.dream.dreambulider.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFile  extends BaseTimeEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_id")
	private Long id;
	private String oriName;
	private String uuidName;
	private String url;
	@JoinColumn(name = "board_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Board board;
}

package codehows.dream.dreambulider.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HashTag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hashTag_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	private String hashTag;

//	@Builder
//	public HashTag(Board board, String hashtag) {
//		this.board = board;
//		this.hashtag = hashtag;
//	}

	public HashTag(Board board, String hashTag) {
		this.board = board;
		this.hashTag = hashTag;
	}

	public void update(String hashTag) {
		this.hashTag = hashTag;
	}
}

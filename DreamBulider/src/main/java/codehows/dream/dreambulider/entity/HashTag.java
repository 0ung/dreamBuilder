package codehows.dream.dreambulider.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

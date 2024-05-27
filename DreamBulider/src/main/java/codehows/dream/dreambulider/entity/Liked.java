package codehows.dream.dreambulider.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Liked extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "liked_id")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@Column(nullable = true)
	private boolean isLike;

	@Builder
	public Liked(Board board, Member member, Boolean isLike) {
		this.board = board;
		this.member = member;
		this.isLike = isLike;
	}
	public void update(Boolean isLike) {
		this.isLike = isLike;
    }
}

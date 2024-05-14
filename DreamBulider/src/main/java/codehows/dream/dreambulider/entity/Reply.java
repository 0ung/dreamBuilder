package codehows.dream.dreambulider.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reply_id")
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String comment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@Column(nullable = false)
	private boolean invisible;

	@OneToMany(mappedBy = "reply", fetch = FetchType.LAZY)
	public List<NestedReply> nestedReplies;
	public void update(String comment) {
		this.comment = comment;
	}
}

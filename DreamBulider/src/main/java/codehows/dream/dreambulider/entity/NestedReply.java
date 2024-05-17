package codehows.dream.dreambulider.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class NestedReply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NestedReply_id")
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String comment;

	@Column(nullable = false)
	private boolean invisible= false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_id")
	private Reply reply;



	/* 대댓글 수정 */
	public void update(String comment) {
		this.comment = comment;
	}
	/* 대댓글 삭제(비활성화) */
	public void delete() {
		this.invisible = true;
	}
}


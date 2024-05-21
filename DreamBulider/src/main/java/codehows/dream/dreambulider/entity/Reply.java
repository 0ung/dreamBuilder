package codehows.dream.dreambulider.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
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

	@Column(nullable = false)
	private boolean invisible = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@Column(name = "reg_date")
	@CreatedDate
	private LocalDateTime createdDate;

	@Column(name = "update_date")
	@LastModifiedDate
	private LocalDateTime modifiedDate;

	public void update(String comment) {
		this.comment = comment;
		this.modifiedDate = modifiedDate.now();
	}

	public void delete() {
		this.invisible = true;
		this.modifiedDate = modifiedDate.now();
	}
}

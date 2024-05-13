package codehows.dream.dreambulider.entity;

import java.sql.Date;

import codehows.dream.dreambulider.constats.Authority;
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
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	@Column(nullable = false)
	private boolean invisible = false;

	private Authority deleteBy;
	private boolean deadLine;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private Long cnt;

	@Builder
	public Board(String title, String content, Date endDate) {
		this.title = title;
		this.content = content;
		this.endDate = endDate;
	}

	public void update(String title, String content, Date endDate) {
		this.title = title;
		this.content = content;
		this.endDate = endDate;
	}

	//게시글 비활성화(삭제)
	public void update1() {
		this.invisible = true;
	}
}

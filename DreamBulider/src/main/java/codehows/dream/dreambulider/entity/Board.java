package codehows.dream.dreambulider.entity;

import codehows.dream.dreambulider.constats.Authority;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board extends BaseTimeEntity{
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
	@ColumnDefault("false")
	private boolean invisible;

	@Enumerated(EnumType.STRING)
	private Authority deleteBy;

	private boolean deadLine;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private Long cnt = 0L;

	//	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	//	private List<HashTag> hashTags;

	@Builder
	public Board(String title, String content, Date endDate, Member member) {
		this.title = title;
		this.content = content;
		this.endDate = endDate;
		this.member = member;
	}

	public void update(String title, String content, Date endDate) {
		this.title = title;
		this.content = content;
		this.endDate = endDate;
	}

	//게시글 비활성화(삭제)
	public void updateInvisible() {
		this.invisible = true;
		this.deleteBy = Authority.ROLE_USER;
	}
}

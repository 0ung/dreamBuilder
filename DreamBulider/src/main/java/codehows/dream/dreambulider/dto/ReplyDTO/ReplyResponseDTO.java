package codehows.dream.dreambulider.dto.ReplyDTO;

import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedResponseDTO;
import codehows.dream.dreambulider.entity.Reply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyResponseDTO {

	private Long id;
	private String comment;
	private boolean invisible;
	private String nickname;
	private LocalDateTime regTime;
	private LocalDateTime updateTime;
	private List<NestedResponseDTO> nestReply;

	public ReplyResponseDTO(Reply reply) {
		this.id = reply.getId();
		this.comment = reply.getComment();
		this.nickname = reply.getMember().getName();
		this.regTime = reply.getRegTime();
		this.updateTime = reply.getUpdateTime();
		this.invisible = reply.isInvisible();
	}

	public static ReplyResponseDTO entityToDTO(Reply reply, List<NestedResponseDTO> nestedReply) {
		return ReplyResponseDTO.builder()
			.id(reply.getId())
			.comment(reply.getComment())
			.nickname(reply.getMember().getName())
			.regTime(reply.getRegTime())
			.updateTime(reply.getUpdateTime())
			.invisible(reply.isInvisible())
			.nestReply(nestedReply)
			.build();
	}
}

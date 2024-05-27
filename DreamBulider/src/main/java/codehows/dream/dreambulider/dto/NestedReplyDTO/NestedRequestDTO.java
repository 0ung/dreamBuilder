package codehows.dream.dreambulider.dto.NestedReplyDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NestedRequestDTO {

	private String comment;
	private boolean invisible ;
	private Long replyId;
	private LocalDateTime regDate = LocalDateTime.now();

}

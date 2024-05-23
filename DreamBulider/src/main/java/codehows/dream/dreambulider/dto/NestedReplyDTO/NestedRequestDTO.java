package codehows.dream.dreambulider.dto.NestedReplyDTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

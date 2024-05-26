package codehows.dream.dreambulider.dto.ReplyDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyRequestDTO {

    private String comment;
    private boolean invisible;
    private Long boardId;
}

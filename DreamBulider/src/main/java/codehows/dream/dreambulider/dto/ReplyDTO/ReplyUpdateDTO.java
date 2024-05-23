package codehows.dream.dreambulider.dto.ReplyDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReplyUpdateDTO {

    private String comment;
    private Long boardId;
    private LocalDateTime updateDate = LocalDateTime.now();
}

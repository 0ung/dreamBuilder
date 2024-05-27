package codehows.dream.dreambulider.dto.ReplyDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReplyUpdateDTO {

    private String comment;
    private Long boardId;
}

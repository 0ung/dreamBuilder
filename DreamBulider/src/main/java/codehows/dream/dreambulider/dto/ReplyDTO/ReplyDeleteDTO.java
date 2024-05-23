package codehows.dream.dreambulider.dto.ReplyDTO;

import codehows.dream.dreambulider.entity.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReplyDeleteDTO {

    private String comment;
    private boolean invisible = true;
    private LocalDateTime updateDate = LocalDateTime.now();

    public ReplyDeleteDTO(Reply reply) {
        this.comment = reply.getComment();

    }
}


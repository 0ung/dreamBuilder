package codehows.dream.dreambulider.dto.ReplyDTO;


import codehows.dream.dreambulider.entity.Reply;
import lombok.Getter;

@Getter
public class ReplyResponseDTO {

    private final String comment;

    public ReplyResponseDTO(Reply reply) {
        this.comment = reply.getComment();
    }
}

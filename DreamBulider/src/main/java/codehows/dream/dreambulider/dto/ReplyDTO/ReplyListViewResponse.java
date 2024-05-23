package codehows.dream.dreambulider.dto.ReplyDTO;

import codehows.dream.dreambulider.entity.Reply;
import lombok.Getter;

@Getter
public class ReplyListViewResponse {

    private final Long id;
    private final String comment;
    //private final boolean invisible;
    public ReplyListViewResponse(Reply reply) {
        this.id = reply.getId();
        this.comment = reply.getComment();
       // this.invisible = reply.isInvisible();
    }
}

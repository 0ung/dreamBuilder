package codehows.dream.dreambulider.dto.ReplyDTO;


import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.entity.Reply;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyResponseDTO {

    private final Long id;
    private final String comment;
    private final boolean invisible;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private String MemberName;


    public ReplyResponseDTO(Reply reply) {
        this.id = reply.getId();
        this.comment = reply.getComment();
        this.MemberName = reply.getMember().getName();
        this.regTime = reply.getRegTime();
        this.updateTime = reply.getUpdateTime();
        this.invisible = reply.isInvisible();
    }
}

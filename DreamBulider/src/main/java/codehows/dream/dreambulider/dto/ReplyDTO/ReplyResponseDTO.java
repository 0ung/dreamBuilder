package codehows.dream.dreambulider.dto.ReplyDTO;


import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.entity.Reply;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyResponseDTO {

    //private Member member;
    //private final Long memberId;
    private final Long id;
    private final String comment;
    private final boolean invisible;
    private LocalDateTime regDate = LocalDateTime.now();
    private LocalDateTime updateDate = LocalDateTime.now();
    private String MemberName;


    public ReplyResponseDTO(Reply reply) {
        //this.memberId = member.getId();
        this.id = reply.getId();
        this.comment = reply.getComment();
        this.MemberName = reply.getMember().getName();
        this.regDate = reply.getCreatedDate();
        this.updateDate =reply.getModifiedDate();
        this.invisible = reply.isInvisible();
    }
}

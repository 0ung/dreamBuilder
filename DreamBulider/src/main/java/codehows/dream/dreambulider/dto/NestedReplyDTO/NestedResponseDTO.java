package codehows.dream.dreambulider.dto.NestedReplyDTO;


import codehows.dream.dreambulider.entity.NestedReply;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NestedResponseDTO {

    private Long id;
    private String comment;
    private boolean invisible;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private String MemberName;

    @Builder
    public NestedResponseDTO(NestedReply nestedReply) {
        this.id = nestedReply.getId();
        this.comment = nestedReply.getComment();
        this.MemberName = nestedReply.getMember().getName();
        this.regTime = nestedReply.getRegTime();
        this.updateTime = nestedReply.getUpdateTime();
        this.invisible = nestedReply.isInvisible();
    }
}

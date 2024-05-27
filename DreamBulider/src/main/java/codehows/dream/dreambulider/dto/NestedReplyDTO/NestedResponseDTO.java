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
    private String nickname;

    @Builder
    public NestedResponseDTO(NestedReply nestedReply) {
        this.id = nestedReply.getId();
        this.comment = nestedReply.getComment();
        this.nickname = nestedReply.getMember().getName();
        this.invisible = nestedReply.isInvisible();
        this.regTime = nestedReply.getRegTime();
        this.updateTime = nestedReply.getUpdateTime();
    }
}

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
    private LocalDateTime regDate = LocalDateTime.now();
    private LocalDateTime updateDate = LocalDateTime.now();
    private String MemberName;

    @Builder
    public NestedResponseDTO(NestedReply nestedReply) {
        this.id = nestedReply.getId();
        this.comment = nestedReply.getComment();
        this.MemberName = nestedReply.getMember().getName();
        this.regDate = nestedReply.getCreatedDate();
        this.updateDate = nestedReply.getModifiedDate();
        this.invisible = nestedReply.isInvisible();
    }
}

package codehows.dream.dreambulider.dto.NestedReplyDTO;


import codehows.dream.dreambulider.entity.NestedReply;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NestedResponseDTO {

    private final String comment;

    @Builder
    public NestedResponseDTO(NestedReply nestedReply) {
        this.comment = nestedReply.getComment();
    }
}

package codehows.dream.dreambulider.dto.NestedReplyDTO;


import codehows.dream.dreambulider.entity.NestedReply;
import codehows.dream.dreambulider.entity.Reply;
import lombok.*;
import org.springframework.boot.context.properties.bind.Nested;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NestedRequestDTO {

    private String comment;
    private Reply reply;

    public NestedReply toEntity() {

        return NestedReply.builder()
                .comment(comment)
                .reply(reply)
                .build();
    }

}

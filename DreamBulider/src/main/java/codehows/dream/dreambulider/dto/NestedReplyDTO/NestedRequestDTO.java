package codehows.dream.dreambulider.dto.NestedReplyDTO;


import codehows.dream.dreambulider.entity.NestedReply;
import codehows.dream.dreambulider.entity.Reply;
import lombok.*;
import org.springframework.boot.context.properties.bind.Nested;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NestedRequestDTO {

    private String comment;
    private boolean invisible;
    private Reply reply;
}

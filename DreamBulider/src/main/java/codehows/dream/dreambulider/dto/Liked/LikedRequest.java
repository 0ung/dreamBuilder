package codehows.dream.dreambulider.dto.Liked;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikedRequest {
//좋아요 버튼 누르고 취소할때 받는 dto
    private Long boardId;
}

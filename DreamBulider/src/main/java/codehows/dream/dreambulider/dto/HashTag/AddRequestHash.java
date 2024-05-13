package codehows.dream.dreambulider.dto.HashTag;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddRequestHash {

    private String hashTag;
    private Long boardId;

//    public HashTag toEntity(Board board) {
//        return HashTag.builder()
//                .board(board)
//                .hashtag(hashTag)
//                .build();
//    }
}

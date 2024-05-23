package codehows.dream.dreambulider.dto.Board;

import codehows.dream.dreambulider.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyBoardListDTO {

    private Long id;
    private String title;
    private Long cnt;

    public MyBoardListDTO(Board board) {
        this.id = board.getId();
        this.title =board.getTitle();
        this.cnt = board.getCnt();
    }

}

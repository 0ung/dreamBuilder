package codehows.dream.dreambulider.dto.Board;

import codehows.dream.dreambulider.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardListResponseDTO {

    private Long id;
    private String title;
    private Date endDate;
    private List<String> hashTags;

    public BoardListResponseDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.endDate = board.getEndDate();
        this.hashTags = hashTags;
    }

}

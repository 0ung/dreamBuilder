package codehows.dream.dreambulider.dto.Board;

import codehows.dream.dreambulider.constats.Authority;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
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
public class BoardResponseDTO {

    private Long id;
    private String title;
    private String content;
    private Date endDate;
    private List<String> hashTags;

    public BoardResponseDTO(Board board, List<String> hashTags) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.endDate = board.getEndDate();
        this.hashTags = hashTags;
    }

}

package codehows.dream.dreambulider.dto.Board;

import codehows.dream.dreambulider.constats.Authority;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
import lombok.Getter;

import java.sql.Date;
import java.util.List;

@Getter
public class BoardResponseDTO {

    private Long id;
    private String title;
    private String content;
    private Date endDate;
    private boolean invisible;
    private Authority deleteBy;
    private boolean deadLine;
    private Long cnt;
    private List<HashTag> hashTags;

    public BoardResponseDTO(Board board, List<HashTag> hashTags) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.endDate = board.getEndDate();
        this.invisible = board.isInvisible();
        this.deleteBy = board.getDeleteBy();
        this.deadLine = board.isDeadLine();
        this.cnt = board.getCnt();
        this.hashTags = hashTags;
    }
}

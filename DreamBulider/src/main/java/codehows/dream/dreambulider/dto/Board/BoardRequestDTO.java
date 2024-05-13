package codehows.dream.dreambulider.dto.Board;

import codehows.dream.dreambulider.entity.HashTag;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class BoardRequestDTO {
    private String title;
    private String content;
    private Date endDate;
    private List<HashTag> hashTags;
}

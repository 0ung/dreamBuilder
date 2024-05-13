package codehows.dream.dreambulider.dto.Board;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class AddRequestBoard {
    private String title;
    private String content;
    private Date endDate;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .endDate(endDate)
                .build();
    }

}

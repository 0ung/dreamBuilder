package codehows.dream.dreambulider.dto.Board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestBoard {

    private String title;
    private String content;
    private Date endDate;
}

package codehows.dream.dreambulider.dto.Board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestBoard {
    //아마도 안쓰는거?
    private String title;
    private String content;
    private Date endDate;
}

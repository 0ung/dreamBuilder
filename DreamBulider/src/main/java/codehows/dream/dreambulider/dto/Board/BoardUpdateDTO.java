package codehows.dream.dreambulider.dto.Board;

import codehows.dream.dreambulider.dto.HashTag.UpdateRequestHash;
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
public class BoardUpdateDTO {

    private String title;
    private String content;
    private Date endDate;
   private List<UpdateRequestHash> hashTags;
//    private List<String> hashTags;

}

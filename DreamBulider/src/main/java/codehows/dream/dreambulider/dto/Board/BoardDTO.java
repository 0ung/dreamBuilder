package codehows.dream.dreambulider.dto.Board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class BoardDTO {
    //게시글 수정 ( board와 hashTag 반환하는 dto)
    private Long id;
    private String title;
    private String content;
    private Date endDate;
    private List<String> hashTags;

    public BoardDTO(String title, String content, Date endDate, List<String> hashTags) {
        this.title = title;
        this.content = content;
        this.endDate = endDate;
        this.hashTags = hashTags;
    }
}

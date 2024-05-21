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
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDTO {
    //게시글 상세조회 dto

    private Long id;
    private String title;
    private String content;
    private Date endDate;
    private Long cnt;
    private List<String> hashTags;
    private List<Map<String,String>> file;
    public BoardResponseDTO(Board board, List<String> hashTags,List<Map<String,String>> file) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.endDate = board.getEndDate();
        this.cnt = board.getCnt();
        this.hashTags = hashTags;
        this.file = file;
    }

}
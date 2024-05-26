package codehows.dream.dreambulider.dto.Board;

import codehows.dream.dreambulider.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BoardAdminListDTO {
    //관리자 게시물 조회 dto
    private Long id;
    private String title;
    private Date endDate;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private boolean invisible;

//    public BoardAdminListDTO(Long id, String title, Date endDate, LocalDateTime regTime, LocalDateTime updateTime, boolean invisible) {
//        this.id = id;
//        this.title = title;
//        this.endDate = endDate;
//        this.regTime = regTime;
//        this.updateTime = updateTime;
//        this.invisible = invisible;
//    }

    public BoardAdminListDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.endDate = board.getEndDate();
        this.regTime = board.getRegTime();
        this.updateTime = board.getUpdateTime();
        this.invisible = board.isInvisible();
    }
}

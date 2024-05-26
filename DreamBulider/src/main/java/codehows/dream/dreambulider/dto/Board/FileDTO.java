package codehows.dream.dreambulider.dto.Board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    //관리자 파일 설정 dto
    private String uploadSize;
    private Integer uploadNum;
    private String docExtension;
    private String imageExtension;
    private String videoExtension;

}

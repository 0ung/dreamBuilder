package codehows.dream.dreambulider.dto.Board;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Long id;
    private String uploadSize;
    private Integer uploadNum;
    private String docExtension;
    private String imageExtension;
    private String videoExtension;

}
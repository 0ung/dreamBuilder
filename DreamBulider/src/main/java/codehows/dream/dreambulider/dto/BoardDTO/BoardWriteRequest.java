package codehows.dream.dreambulider.dto.BoardDTO;


import codehows.dream.dreambulider.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 기본생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받고
@Getter // 모든 필드에 접근자 메서드 주고
public class BoardWriteRequest {

    private String title;
    private String content;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .build();
    }
}

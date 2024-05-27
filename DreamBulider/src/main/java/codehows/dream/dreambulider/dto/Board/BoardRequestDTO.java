package codehows.dream.dreambulider.dto.Board;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.service.BoardFileService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequestDTO {
    //게시글 추가 dto

    private String title;
    private String content;
    private Date endDate;
    private List<String> hashTags;

    public void replaceTempUrlsWithPermanentUrls(BoardFileService boardFileService) throws IOException {
        if (content != null) {
            // 정규식을 사용하여 임시 URL을 찾습니다.
            Pattern pattern = Pattern.compile("/temp/([\\w-]+\\.[\\w]+)");
            Matcher matcher = pattern.matcher(content);
            StringBuffer updatedContent = new StringBuffer();

            while (matcher.find()) {
                String tempUrl = matcher.group();
                // 임시 파일을 원본 파일로 이동합니다.
                boardFileService.moveFileToPermanentLocation(tempUrl);
                // 원본 URL로 변경합니다.
                String permanentUrl = tempUrl.replace("/temp/", "/files/");
                matcher.appendReplacement(updatedContent, permanentUrl);
            }
            matcher.appendTail(updatedContent);
            this.setContent(updatedContent.toString());
        }
    }

    public BoardRequestDTO(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.endDate = board.getEndDate();
    }
}

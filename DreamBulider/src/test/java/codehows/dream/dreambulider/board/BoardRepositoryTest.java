package codehows.dream.dreambulider.board;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.HashTagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Test void select(){
        System.out.println(hashTagRepository.findByBoardId(1L));
    }

    @Test
    public void testFindByTitleOrAuthor() {
        Pageable pageable = PageRequest.of(0, 10);
        String keyword = "더미";
        String escapedKeyword = keyword.replace("\\", "\\\\");
        Page<Board> results = boardRepository.findByTitleOrAuthor(escapedKeyword, pageable);
        System.out.println(results);
    }
}

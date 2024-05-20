package codehows.dream.dreambulider.board;

import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.HashTagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private final HashTagRepository hashTagRepository;

    public BoardRepositoryTest(HashTagRepository hashTagRepository) {
        this.hashTagRepository = hashTagRepository;
    }

    @Test void select(){
        System.out.println(hashTagRepository.findByBoardId(1L));
    }
}

package codehows.dream.dreambulider.board;

import codehows.dream.dreambulider.repository.BoardFileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardFileRepositoryTest {

	@Autowired
	private BoardFileRepository boardFileRepository;

	@Test
	void findByBoardId() {
		boardFileRepository.findByBoardId(1L);
	}
}

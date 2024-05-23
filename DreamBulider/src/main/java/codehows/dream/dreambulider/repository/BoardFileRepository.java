package codehows.dream.dreambulider.repository;

import java.util.List;

import codehows.dream.dreambulider.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFile,Long> {
	public List<BoardFile> findByBoardId(Long boardId);
}

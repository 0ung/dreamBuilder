package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile,Long> {
	public List<BoardFile> findByBoardId(Long boardId);
}

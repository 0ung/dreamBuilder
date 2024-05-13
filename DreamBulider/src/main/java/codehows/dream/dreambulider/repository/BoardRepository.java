package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
}

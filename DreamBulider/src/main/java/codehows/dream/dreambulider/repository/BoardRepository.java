package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {

    //List<Board> findAllByBoardByTitle();

    Page<Board> findAll(Pageable pageable);
}

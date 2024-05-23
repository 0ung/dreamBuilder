package codehows.dream.dreambulider.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

	Page<Reply> findByBoardId(Long boardId, Pageable pageable);

	Long countReplyByBoardId(Long boardId);

    Page<Reply> findByBoard(Board board, Pageable pageable);

}

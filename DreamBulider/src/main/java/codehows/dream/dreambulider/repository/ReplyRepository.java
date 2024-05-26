package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

	Page<Reply> findByBoardId(Long boardId, Pageable pageable);

	Long countReplyByBoardId(Long boardId);

    Page<Reply> findByBoard(Board board, Pageable pageable);
    //이번 달 작성한 댓글 개수
    @Query(value = "SELECT COUNT(*) FROM reply WHERE member_id = :memberId AND MONTH(regTime) = MONTH(CURRENT_DATE)", nativeQuery = true)
    Long countReplyByMember(@Param("memberId") Long memberId);

	Page<Reply> findAllBy(Pageable pageable);
}

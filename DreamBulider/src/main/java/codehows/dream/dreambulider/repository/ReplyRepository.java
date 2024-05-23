package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.NestedReply;
import codehows.dream.dreambulider.entity.Reply;
import codehows.dream.dreambulider.service.ReplyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    //이번 달 작성한 댓글 개수
    @Query(value = "SELECT COUNT(*) FROM reply WHERE member_id = :memberId AND MONTH(regTime) = MONTH(CURRENT_DATE)", nativeQuery = true)
    Long countReplyByMember(@Param("memberId") Long memberId);

}

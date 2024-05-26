package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.NestedReply;
import codehows.dream.dreambulider.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NestedRepository extends JpaRepository <NestedReply,Long> {

    List<NestedReply> getNestedReplyByReplyOrderById(Reply reply);
    List<NestedReply> findByReplyIdOrderByIdDesc(Long replyId);

}

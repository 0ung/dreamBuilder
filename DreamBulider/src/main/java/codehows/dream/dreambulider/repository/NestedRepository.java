package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.NestedReply;
import codehows.dream.dreambulider.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NestedRepository extends JpaRepository <NestedReply,Long> {

    List<NestedReply> getNestedReplyByReplyOrderById(Reply reply);
    Optional<NestedReply> findByReplyIdAndId(Long replyId, Long id);

}

package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.NestedReply;
import codehows.dream.dreambulider.entity.Reply;
import codehows.dream.dreambulider.service.ReplyService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {


}

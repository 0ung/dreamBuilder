package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    List<HashTag> findByBoardId(Long boardId);

}

package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    @Query(value = "SELECT hash_tag FROM Hash_tag WHERE board_id = :boardId", nativeQuery = true)
    List<String> findByBoardId(@Param("boardId") Long boardId);

    @Query(value = "SELECT * FROM Hash_tag WHERE board_id = :boardId", nativeQuery = true)
    List<HashTag> findHashTagsByBoardId(@Param("boardId") Long boardId);

    @Modifying
    @Query(value = "DELETE FROM Hash_tag WHERE board_id = :boardId", nativeQuery = true)
    void deleteByBoardId(@Param("boardId") Long boardId);

    @Query(value = "SELECT board_id FROM Hash_tag WHERE hash_tag LIKE %:hashTag%", nativeQuery = true)
    List<Long> findHashTagByHashTag(@Param("hashTag") String hashTag);

}

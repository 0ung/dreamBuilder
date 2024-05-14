package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    @Query(value = "SELECT hash_tag FROM Hash_tag WHERE board_id = :boardId", nativeQuery = true)
    List<String> findByBoardId(@Param("boardId") Long boardId);

    @Query(value = "SELECT hash_tag FROM Hash_tag WHERE board_id = :boardId", nativeQuery = true)
    List<String> findHashTagsByBoardId(@Param("boardId") Long boardId);

    @Modifying
    @Query(value = "DELETE FROM Hash_tag WHERE board_id = :boardId", nativeQuery = true)
    void deleteByBoardId(@Param("boardId") Long boardId);

}

package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long> {

    @Query(value = "SELECT is_like FROM liked WHERE board_id = :boardId", nativeQuery = true)
    List<Boolean> findByBoardId(@Param("boardId") Long boardId);

    @Query(value = "SELECT is_like FROM liked WHERE board_id = :boardId AND member_id = :memberId", nativeQuery = true)
    Boolean findByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    //게시판 좋아요 총 개수
    @Query(value = "SELECT COUNT(*) FROM liked WHERE board_id = :boardId AND is_like = true", nativeQuery = true)
    Long countByBoardId(@Param("boardId") Long boardId);

    @Query(value = "SELECT is_Like FROM Liked WHERE board_id = :boardId AND member_id = :memberId", nativeQuery = true)
    Boolean findIsLikeByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    @Query(value = "SELECT * FROM Liked WHERE board_id = :boardId AND member_id = :memberId", nativeQuery = true)
    Liked findAllByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);


}

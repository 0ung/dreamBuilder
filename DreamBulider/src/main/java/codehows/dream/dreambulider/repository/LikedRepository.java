package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long> {

    @Query(value = "SELECT is_like FROM liked WHERE board_id = :boardId", nativeQuery = true)
    Boolean findByBoardId(@Param("boardId") Long boardId);

    //좋아요 true/false 출력
    @Query(value = "SELECT is_like FROM liked WHERE board_id = :boardId AND member_id = :memberId", nativeQuery = true)
    Boolean findByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    //게시판 좋아요 총 개수
    @Query(value = "SELECT COUNT(*) FROM liked WHERE board_id = :boardId AND is_like = true", nativeQuery = true)
    Long countByBoardId(@Param("boardId") Long boardId);

    //이번 달 받은 좋아요 개수
    @Query(value = "SELECT COUNT(*) FROM liked WHERE member_id = :memberId And is_like = true AND MONTH(regTime) = MONTH(CURRENT_DATE)", nativeQuery = true)
    Long countByMemberId(@Param("memberId") Long memberId);

    //좋아요 추가/삭제
    @Query(value = "SELECT is_Like FROM Liked WHERE board_id = :boardId AND member_id = :memberId", nativeQuery = true)
    Boolean findIsLikeByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    //좋아요 취소
    @Query(value = "SELECT * FROM Liked WHERE board_id = :boardId AND member_id = :memberId", nativeQuery = true)
    Liked findAllByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);


//    //좋아요 상위 5개 출력
//    @Query(value = "select board_id from liked where is_like=true group by board_id order by count(*) desc limit 5", nativeQuery = true)
//    List<Long> groupByBoardId();
/*
    @Query("SELECT l.board_id AS like_count" +
            "FROM liked l" +
            "JOIN board b ON l.board_id = b.board_id" +
            "WHERE l.is_like = TRUE AND b.invisible = FALSE" +
            "GROUP BY l.board_id" +
            "ORDER BY like_count DESC" +
            "LIMIT 5")
    List<Long> groupByBoardId();
*/


    @Query(value = "SELECT l.board_id " +
            "FROM liked l " +
            "JOIN (SELECT board_id FROM board WHERE invisible = false) b " +
            "ON b.board_id = l.board_id " +
            "WHERE l.is_like = true " +
            "GROUP BY l.board_id " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Long> findLikedVisibleBoards();
}

package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByMemberIdAndInvisibleFalse(Long memberId);

    // Pagination
    Page<Board> findByInvisibleFalseAndEndDateAfter(Pageable pageable, Date currentDate);

    Long countByMemberIdAndInvisibleFalse(Long memberId);

    // Excel file creation
    @Query(value = "select * from Board where invisible=1 AND delete_by = 1", nativeQuery = true)
    List<Board> findAll();

    // Find member by board ID
    @Query("SELECT b.member FROM Board b WHERE b.id = :id")
    Member findMemberByBoardId(@Param("id") Long id);

    // My page post list
    Page<Board> findByMemberIdAndInvisibleFalse(Long id, Pageable pageable);

    // Search by title
    Page<Board> findByTitleContainingIgnoreCaseAndInvisibleFalseAndEndDateAfter(String title, Pageable pageable,Date date);

    // Search by content
    Page<Board> findByContentContainingIgnoreCaseAndInvisibleFalseAndEndDateAfter(String content, Pageable pageable,Date date);

    // Search by member (assuming member is a field in Board)
    Page<Board> findByMember_NameContainingIgnoreCaseAndInvisibleFalseAndEndDateAfter(String memberName, Pageable pageable,Date date);

    // Hashtag search
    @Query("select b from Board b where b.id = :boardId and b.invisible = false and b.endDate > CURRENT DATE ")
    Page<Board> hashTagSearch(@Param("boardId") Long boardId, Pageable pageable);

    // Search by title or content
    @Query("select b from Board b where (b.title like %:keyword% or b.content like %:keyword%) and b.invisible = false  and b.endDate > CURRENT DATE ")
    Page<Board> titleAndContentSearch(@Param("keyword") String keyword, Pageable pageable);

    // Search by title or member name
    @Query("select b from Board b where (b.title like %:keyword% or b.member.name like %:keyword%) and b.invisible = false  and b.endDate > CURRENT DATE ")
    Page<Board> titleAndNameSearch(@Param("keyword") String keyword, Pageable pageable);

    // Search by content or member name
    @Query("select b from Board b where (b.content like %:keyword% or b.member.name like %:keyword%) and b.invisible = false  and b.endDate > CURRENT DATE ")
    Page<Board> contentAndMemberSearch(@Param("keyword") String keyword, Pageable pageable);

    // Search by title, content, or member name
    @Query("select b from Board b where (b.title like %:keyword% or b.content like %:keyword% or b.member.name like %:keyword%) and b.invisible = false  and b.endDate > CURRENT DATE ")
    Page<Board> allSearch(@Param("keyword") String keyword, Pageable pageable);

    // Increment view count
    @Modifying
    @Query("update Board b set b.cnt = b.cnt + 1 where b.id = :id")
    void incrementCnt(@Param("id") Long id);

    // Get view count by ID
    @Query("select b.cnt from Board b where b.id = :id")
    Long getCntById(@Param("id") Long id);

    // Count posts made this month by member
    @Query(value = "SELECT COUNT(*) FROM board WHERE member_id = :memberId AND MONTH(reg_time) = MONTH(CURRENT_DATE)", nativeQuery = true)
    Long countBoardByMember(@Param("memberId") Long memberId);
}

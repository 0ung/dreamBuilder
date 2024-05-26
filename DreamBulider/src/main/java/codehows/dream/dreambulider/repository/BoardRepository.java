package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

    //페이징
    Page<Board> findAllByInvisibleFalse(Pageable pageable);

    //엑셀 파일 만들기
    @Query(value = "select * from Board where invisible=1 AND delete_by = 1", nativeQuery = true)
    List<Board> findAll();

    //보드 아이디로 멤버 조회
    @Query("SELECT b.member FROM Board b WHERE b.id = :id")
    Member findMemberByBoardId(Long id);

    //마이페이지 작성 글 목록
    @Query("SELECT b FROM Board b WHERE b.id = :id and b.invisible=false")
    Page<Board> findBoardByMemberIdAndInvisibleFalse(Long id, Pageable pageable);

   // @Query(SELECT b.invisible  FROM Board b WHERE b.id = :id" )
    //BoardAdminUpdateDTO findByBoardId(Long id);

    //제목 조회
    Page<Board> findByTitleContainingIgnoreCaseAndInvisibleFalse(String title, Pageable pageable);

    //내용 조회
    Page<Board> findByContentContainingIgnoreCaseAndInvisibleFalse(String content, Pageable pageable);

    //맴버 조회
    Page<Board> findByMemberContainingIgnoreCaseAndInvisibleFalse(String keyword, Pageable pageable);

    //해시태그 검색
    @Query("select b from Board b where b.id = :boardId and b.invisible=false")
    Page<Board> findByboardIdAndInvisibleFalse(@Param("boardId") Long boardId, Pageable pageable);

    //제목+내용 조회
    @Query("select b from Board b where b.title like %:keyword% or b.content like %:keyword% and b.invisible=false")
    Page<Board> findByTitleOrContentAndInvisibleFalse(@Param("keyword") String keyword, Pageable pageable);

    //제목+작성자 조회
    @Query("select b from Board b where b.title like %:keyword% or b.member.name like %:keyword% and b.invisible=false")
    Page<Board> findByTitleOrMemberAndInvisibleFalse(@Param("keyword") String keyword, Pageable pageable);

    //내용+작성자 조회
    @Query("select b from Board b where b.content like %:keyword% or b.member.name like %:keyword% and b.invisible=false")
    Page<Board> findByContentOrMemberAndInvisibleFalse(@Param("keyword") String keyword, Pageable pageable);

    //제목+내용+작성자 조회
    @Query("select b from Board b where b.title like %:keyword% or b.content like %:keyword% or b.member.name like %:keyword% and b.invisible=false")
    Page<Board> findByTitleOrContentOrMemberInvisibleFalse(@Param("keyword") String keyword, Pageable pageable);

    //조회수 추가
    @Modifying
    @Query("update Board b set b.cnt = b.cnt + 1 where b.id = :id")
    void incrementCnt(@Param("id") Long id);

    //추가된 조회수 값 반환
    @Query("select b.cnt from Board b where b.id = :id")
    Long getCntById(@Param("id") Long id);

    //이번 달 작성한 게시글 개수
    @Query(value = "SELECT COUNT(*) FROM board WHERE member_id = :memberId AND MONTH(regTime) = MONTH(CURRENT_DATE)", nativeQuery = true)
    Long countBoardByMember(@Param("memberId") Long memberId);

    //좋아요 5개 출력
//    @Query(value = "SELECT b.* FROM Board b JOIN (SELECT board_id FROM liked WHERE is_like = true GROUP BY board_id ORDER BY COUNT(*) DESC LIMIT 5) l ON b.id = l.board_id WHERE b.invisible = false", nativeQuery = true)
 //   List<Board> findTop5LikedVisibleBoards();

    /*

    @Query("SELECT l.board_id, COUNT(*) AS like_count" +
    "FROM liked l" +
    "JOIN board b ON l.board_id = b.board_id" +
    "WHERE l.is_like = TRUE AND b.invisible = FALSE" +
    "GROUP BY l.board_id" +
    "ORDER BY like_count DESC" +
    "LIMIT 5")
    List<Board> findTop5LikedVisibleBoards();


    // 좋아요가 많은 상위 5개 게시글 조회
    @Query(value = "SELECT b.* FROM board b " +
            "JOIN (SELECT board_id FROM liked WHERE is_like = true GROUP BY board_id ORDER BY COUNT(*) DESC LIMIT 5) l " +
            "ON b.board_id = l.board_id " +
            "WHERE b.invisible = false", nativeQuery = true)
    List<Board> findTop5LikedVisibleBoards();
*/


}

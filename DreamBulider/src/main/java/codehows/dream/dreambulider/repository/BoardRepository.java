package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Board;

import codehows.dream.dreambulider.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

    //List<Board> findAllByBoardByTitle();

    Page<Board> findAll(Pageable pageable);

    @Query("SELECT b.member FROM Board b WHERE b.id = :id")
    Member findMemberByBoardId(Long id);

//    @Query("SELECT p FROM Board p WHERE p.deadline > :currentDateTime OR p.deadline IS NULL")
//    List<Board> findAllActivePosts(@Param("currentDateTime") LocalDateTime currentDateTime);

    //제목 조회
    Page<Board> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    //내용 조회
    Page<Board> findByContentContainingIgnoreCase(String content, Pageable pageable);
    //맴버 조회
    Page<Board> findByMemberContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("select b from Board b where b.id = :boardId")
    Page<Board> findByboardId(@Param("boardId") Long boardId, Pageable pageable);

    //제목+내용 조회
    @Query("select b from Board b where b.title like %:keyword% or b.content like %:keyword%")
    Page<Board> findByTitleOrContent(@Param("keyword") String keyword, Pageable pageable);

    //제목+작성자 조회
    @Query("select b from Board b where b.title like %:keyword% or b.member.name like %:keyword%")
    Page<Board> findByTitleOrAuthor(@Param("keyword") String keyword, Pageable pageable);

    //내용+작성자 조회
    @Query("select b from Board b where b.content like %:keyword% or b.member.name like %:keyword%")
    Page<Board> findByContentOrAuthor(@Param("keyword") String keyword, Pageable pageable);

    //제목+내용+작성자 조회
    @Query("select b from Board b where b.title like %:keyword% or b.content like %:keyword% or b.member.name like %:keyword%")
    Page<Board> findByTitleOrContentOrAuthor(@Param("keyword") String keyword, Pageable pageable);

    //조회수 추가
    @Modifying
    @Query("update Board b set b.cnt = b.cnt + 1 where b.id = :id")
    void incrementCnt(@Param("id") Long id);

    //추가된 조회수 값 반환
    @Query("select b.cnt from Board b where b.id = :id")
    Long getCntById(@Param("id") Long id);

}

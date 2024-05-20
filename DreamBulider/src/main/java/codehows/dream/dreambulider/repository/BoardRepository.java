package codehows.dream.dreambulider.repository;

import codehows.dream.dreambulider.entity.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board,Long> {

    //List<Board> findAllByBoardByTitle();

    Page<Board> findAll(Pageable pageable);

    //제목 조회
    Page<Board> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    //내용 조회
    Page<Board> findByContentContainingIgnoreCase(String content, Pageable pageable);

    //Page<Board> findByMemberContainingIgnoreCase(String keyword, Pageable pageable);

    //제목+내용 조회
    @Query("select b from Board b where b.title like %:keyword% or b.content like %:keyword%")
    Page<Board> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);

    //제목+작성자 조회
    @Query("select b from Board b where b.title like %:keyword% or b.member.name like %:keyword%")
    Page<Board> findByTitleOrAuthorContaining(@Param("keyword") String keyword, Pageable pageable);

    //내용+작성자 조회
    @Query("select b from Board b where b.content like %:keyword% or b.member.name like %:keyword%")
    Page<Board> findByContentOrAuthorContaining(@Param("keyword") String keyword, Pageable pageable);

    //제목+내용+작성자 조회
    @Query("select b from Board b where b.title like %:keyword% or b.content like %:keyword% or b.member.name like %:keyword%")
    Page<Board> findByTitleOrContentOrAuthorContaining(@Param("keyword") String keyword, Pageable pageable);

    //조회수 추가
    @Modifying
    @Query("update Board b set b.cnt = b.cnt + 1 where b.id = :id")
    void incrementCnt(@Param("id") Long id);

    //추가된 조회수 값 반환
    @Query("select b.cnt from Board b where b.id = :id")
    Long getCntById(@Param("id") Long id);

}

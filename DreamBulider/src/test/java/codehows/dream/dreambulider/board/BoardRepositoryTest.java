package codehows.dream.dreambulider.board;

import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void testFindMemberByBoardId() {
        // Given
        Member member = new Member();
        member.setName("testuser");
        member.setPassword("password");
        member = memberRepository.save(member);

        Board board = new Board();
        board.setTitle("Test Title");
        board.setContent("Test Content");
        board.setMember(member);
        board = boardRepository.save(board);

        // When
        Member foundMember = boardRepository.findMemberByBoardId(board.getId());

        // Then
        assertNotNull(foundMember);
        assertEquals(member.getId(), foundMember.getId());
        assertEquals(member.getUsername(), foundMember.getUsername());
    }

    @Test
    public void testFindByTitleOrAuthor() {
        Pageable pageable = PageRequest.of(0, 10);
        String keyword = "더미";
        String escapedKeyword = keyword.replace("\\", "\\\\");
        Page<Board> results = boardRepository.findByTitleOrAuthor(escapedKeyword, pageable);
        System.out.println(results);
    }
}

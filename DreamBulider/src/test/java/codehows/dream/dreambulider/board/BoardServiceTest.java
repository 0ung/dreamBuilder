//package codehows.dream.dreambulider.board;
//
//import codehows.dream.dreambulider.dto.Board.BoardUpdateDTO;
//import codehows.dream.dreambulider.entity.Board;
//import codehows.dream.dreambulider.entity.Member;
//import codehows.dream.dreambulider.repository.BoardRepository;
//import codehows.dream.dreambulider.repository.MemberRepository;
//import codehows.dream.dreambulider.service.BoardService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.security.Principal;
//import java.sql.Date;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//public class BoardServiceTest {
//
//    @Autowired
//    private BoardService boardService;
//
//    @MockBean
//    private BoardRepository boardRepository;
//
//    @MockBean
//    private MemberRepository memberRepository;
//
//    @Test
//    @WithMockUser(username = "testuser@example.com")
//    public void testUpdateBoard() {
//        // Given
//        Member member = new Member();
//        member.setId(1L);
//        member.setEmail("testuser@example.com");
//        Mockito.when(boardRepository.findMemberByBoardId(1L)).thenReturn(member);
//
//        Board board = new Board();
//        board.setId(1L);
//        board.setTitle("Original Title");
//        board.setContent("Original Content");
//        Mockito.when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
//
//        BoardUpdateDTO updateDTO = new BoardUpdateDTO();
//        updateDTO.setTitle("Updated Title");
//        updateDTO.setContent("Updated Content");
//        updateDTO.setEndDate(Date.valueOf("2020-02-02"));
//
//        Principal principal = Mockito.mock(Principal.class);
//        Mockito.when(principal.getName()).thenReturn("testuser@example.com");
//
//        // When
//        Board updatedBoard = boardService.update(1L, updateDTO, principal);
//
//        // Then
//        assertNotNull(updatedBoard);
//        assertEquals(updateDTO.getTitle(), updatedBoard.getTitle());
//        assertEquals(updateDTO.getContent(), updatedBoard.getContent());
//    }
//
//    @Test
//    @WithMockUser(username = "wronguser@example.com")
//    public void testUpdateBoardUnauthorized() {
//        // Given
//        Member member = new Member();
//        member.setId(1L);
//        member.setEmail("testuser@example.com");
//        Mockito.when(boardRepository.findMemberByBoardId(1L)).thenReturn(member);
//
//        BoardUpdateDTO updateDTO = new BoardUpdateDTO();
//        updateDTO.setTitle("Updated Title");
//        updateDTO.setContent("Updated Content");
//        updateDTO.setEndDate(Date.valueOf("2020-02-02"));
//
//        Principal principal = Mockito.mock(Principal.class);
//        Mockito.when(principal.getName()).thenReturn("wronguser@example.com");
//
//        // When & Then
//        assertThrows(SecurityException.class, () -> {
//            boardService.update(1L, updateDTO, principal);
//        });
//    }
//
//    @Test
//    public void testUpdateBoardNotFound() {
//        // Given
//        Mockito.when(boardRepository.findMemberByBoardId(1L)).thenReturn(null);
//
//        BoardUpdateDTO updateDTO = new BoardUpdateDTO();
//        updateDTO.setTitle("Updated Title");
//        updateDTO.setContent("Updated Content");
//        updateDTO.setEndDate(Date.valueOf("2020-02-02"));
//
//        Principal principal = Mockito.mock(Principal.class);
//        Mockito.when(principal.getName()).thenReturn("testuser@example.com");
//
//        // When & Then
//        assertThrows(IllegalArgumentException.class, () -> {
//            boardService.update(1L, updateDTO, principal);
//        System.out.println();
//        });
//    }
//
//
//}

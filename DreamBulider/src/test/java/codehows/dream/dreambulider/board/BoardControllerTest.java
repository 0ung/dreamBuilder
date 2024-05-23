package codehows.dream.dreambulider.board;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.sql.Date;
import java.util.Collections;
import java.util.Optional;

import codehows.dream.dreambulider.dto.Board.BoardListResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import codehows.dream.dreambulider.controller.BoardController;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.LikedRepository;
import codehows.dream.dreambulider.service.BoardService;
import codehows.dream.dreambulider.service.HashTagService;
import codehows.dream.dreambulider.service.LikedService;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @MockBean
    private HashTagService hashTagService;

    @MockBean
    private LikedService likedService;

    @MockBean
    private BoardRepository boardRepository;

    @MockBean
    private LikedRepository likedRepository;

    @InjectMocks
    private BoardController boardController;

    @Test
    public void testSearchBoardList() throws Exception {
        Principal principal = () -> "test@example.com";

        Board board = new Board();
        board.setId(1L);
        board.setTitle("Test Board");
        board.setEndDate(Date.valueOf("2024-12-31"));

        BoardListResponseDTO dto = new BoardListResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setEndDate(board.getEndDate());
        dto.setHashTags(Collections.emptyList());
        dto.setCnt(0L);
        dto.setLikeList(false);
        dto.setCountLike(0L);

        Page<Board> boardPage = new PageImpl<>(Collections.singletonList(board));
        given(boardService.searchBoard(any(Pageable.class), eq("title"), eq("Test"), eq(principal)))
                .willReturn(Collections.singletonList(dto));
        given(boardService.sorted(any(String.class), anyInt())).willReturn(Pageable.unpaged());

        mockMvc.perform(get("/api/boards/0")
                        .param("search", "Test")
                        .param("criteria", "title")
                        .param("sort", "date")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testLikeList() throws Exception {
        Principal principal = () -> "test@example.com";

        Board board = new Board();
        board.setId(1L);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@example.com");

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(likedRepository.findByBoardIdAndMemberId(anyLong(), anyLong())).willReturn(true);

        Boolean likeStatus = likedService.LikeList(board.getId(), principal);
        assert likeStatus != null && likeStatus;
    }
}

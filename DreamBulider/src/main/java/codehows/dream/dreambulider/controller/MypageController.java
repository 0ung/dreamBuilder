package codehows.dream.dreambulider.controller;

import codehows.dream.dreambulider.dto.Board.BoardAdminListDTO;
import codehows.dream.dreambulider.dto.Board.MyBoardListDTO;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final BoardService boardService;

    //내가 작성한 글 찾기
    @GetMapping("/api/myPage/{page}")
    public ResponseEntity<List<MyBoardListDTO>> findMyBoard(@PathVariable Optional<Integer> page, Principal principal) {
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 10);
        List<MyBoardListDTO> board = boardService.myBoard(principal, pageable)
                .stream()
                .map(MyBoardListDTO::new)
                .toList();
        return ResponseEntity.ok()
                .body(board);

    }
 }

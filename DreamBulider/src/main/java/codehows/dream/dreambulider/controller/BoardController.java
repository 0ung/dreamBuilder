package codehows.dream.dreambulider.controller;


import codehows.dream.dreambulider.dto.BoardDTO.BoardWriteRequest;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/api/boards")
    public ResponseEntity<Board> addBoard(@RequestBody BoardWriteRequest request) {
        Board savedBoard = boardService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedBoard);
    }
}

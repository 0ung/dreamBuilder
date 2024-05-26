package codehows.dream.dreambulider.controller;

import codehows.dream.dreambulider.dto.Board.MyBoardListDTO;
import codehows.dream.dreambulider.service.BoardService;
import codehows.dream.dreambulider.service.LikedService;
import codehows.dream.dreambulider.service.MemberService;
import codehows.dream.dreambulider.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final LikedService likedService;
    private final ReplyService replyService;


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



    //이번 달 받은 좋아요 개수
    @GetMapping("/api/myPage/like")
    public ResponseEntity<Long> countLike(Principal principal) {
       Long count = likedService.likedCount(principal);

        return ResponseEntity.ok()
                .body(count);
    }

    //이번 달 작성한 글 개수
    @GetMapping("/api/myPage/board")
    public ResponseEntity<?> countBoard(Principal principal) {
        Long count = boardService.countBoard(principal);

        return ResponseEntity.ok()
                .body(count);
    }

    //이번 달 작성한 댓글 개수
    @GetMapping("/api/myPage/reply")
    public ResponseEntity<?> countReply(Principal principal) {
        Long count = replyService.countReply(principal);

        return ResponseEntity.ok()
                .body(count);
    }


 }

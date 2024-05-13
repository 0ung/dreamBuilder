package codehows.dream.dreambulider.Controller;

import codehows.dream.dreambulider.dto.Board.BoardRequestDTO;
import codehows.dream.dreambulider.dto.Board.BoardResponseDTO;
import codehows.dream.dreambulider.dto.Board.ResponseBoard;
import codehows.dream.dreambulider.dto.Board.UpdateRequestBoard;
import codehows.dream.dreambulider.dto.HashTag.MemberNoExistExcpetion;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
import codehows.dream.dreambulider.service.BoardService;
import codehows.dream.dreambulider.service.HashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final HashTagService hashTagService;

    //게시글 작성
    @PostMapping("/api/addBoard")
    public ResponseEntity<Board> addBoard(@RequestBody BoardRequestDTO boardDTO) {
        try{
            Board savedBoard = boardService.save(boardDTO);
            boardDTO.getHashTags().forEach(e -> {
                hashTagService.saveHashTag(savedBoard.getId(),e);
            });
//
//            for(HashTag hash : boardDTO.getHashTags()){
//                hashTagService.saveHashTag(savedBoard.getId(), hash);
//            }
        } catch (MemberNoExistExcpetion e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //201 완료 ok
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    //게시글 목록 조회
    @GetMapping("/api/boardList")
    public ResponseEntity<List<ResponseBoard>> findAllBoardList() {
        List<ResponseBoard> boardList = boardService.findAll()
                .stream()
                .map(ResponseBoard::new)
                .toList();

        return ResponseEntity.ok().body(boardList);
    }

//    //게시글 상세 조회
//    @GetMapping("/api/board/{id}")
//    public ResponseEntity<ResponseBoard> findBoard(@PathVariable long id) {
//        Board board = boardService.findById(id);
//
//        return ResponseEntity.ok()
//                .body(new ResponseBoard(board));
//    }

    //게시글 상세 조회
    @GetMapping("/api/board/{id}")
    public ResponseEntity<BoardResponseDTO> findBoard(@PathVariable long id) {
        Board board = boardService.findById(id);
//        HashTag hashTag = hashTagService.findById(id);
        List<HashTag> hashTags = boardService.findHashTagsByBoardId(id);

        //return ResponseEntity.ok().body(responseBoard);

       // return new ResponseEntity<>(HttpStatus.OK);
    }

    //게시글 수정
    @PutMapping("/api/board/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable long id, @RequestBody UpdateRequestBoard requestBoard) {
        Board updatedBoard = boardService.update(id, requestBoard);

        return ResponseEntity.ok()
                .body(updatedBoard);
    }

    //비활성화
    @PutMapping("/api/{id}")
    public ResponseEntity<Board> deleteBoard(@PathVariable long id) {
        Board updatedBoard = boardService.updateInvisible(id);

        return ResponseEntity.ok()
                .body(updatedBoard);
    }
}

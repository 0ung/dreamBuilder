package codehows.dream.dreambulider.Controller;

import codehows.dream.dreambulider.dto.Board.*;
import codehows.dream.dreambulider.dto.HashTag.MemberNoExistExcpetion;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
import codehows.dream.dreambulider.service.BoardService;
import codehows.dream.dreambulider.service.HashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BoardController {
    /*
    *
    * */

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
    @GetMapping("/api/boardList/{page}")
    public ResponseEntity<?> findAllBoardList(@PathVariable Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Board> boardList = boardService.findAll(pageable);

        List<BoardListResponseDTO> list = new ArrayList<>();
        for (Board board : boardList) {
            BoardListResponseDTO listResponseDTO = new BoardListResponseDTO();
            listResponseDTO.setId(board.getId());
            listResponseDTO.setTitle(board.getTitle());
            listResponseDTO.setEndDate(board.getEndDate());
            listResponseDTO.setHashTags(hashTagService.findAll(board.getId()));
            list.add(listResponseDTO);
        }
        return ResponseEntity.ok().body(list);
   }

    //게시글 상세 조회
    @GetMapping("/api/board/{id}")
    public ResponseEntity<BoardResponseDTO> findBoard(@PathVariable long id) {
        Board board = boardService.findById(id);
//        HashTag hashTag = hashTagService.findById(id);
        List<String> hashTags = hashTagService.findById(id);
        BoardResponseDTO dt = new BoardResponseDTO(board, hashTags);

        //return ResponseEntity.ok().body(responseBoard);
        return ResponseEntity.ok()
                .body(dt);
    }

    //게시글 수정
    @PutMapping("/api/board/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable long id, @RequestBody BoardUpdateDTO requestBoard) {
            Board updatedBoard = boardService.update(id, requestBoard);

//            requestBoard.getHashTags().forEach(e -> {
//                hashTagService.updateHash(id, e);
//            });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //비활성화
/**
    @PutMapping("/api/{id}")
    public ResponseEntity<Board> deleteBoard(@PathVariable long id) {
        Board updatedBoard = boardService.updateInvisible(id);

        return ResponseEntity.ok()
                .body(updatedBoard);
    }
 */
}
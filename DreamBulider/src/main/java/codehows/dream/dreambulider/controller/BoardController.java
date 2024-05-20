package codehows.dream.dreambulider.controller;

import codehows.dream.dreambulider.dto.Board.*;
import codehows.dream.dreambulider.dto.HashTag.MemberNoExistExcpetion;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.repository.HashTagRepository;
import codehows.dream.dreambulider.repository.MemberRepository;
import codehows.dream.dreambulider.service.BoardFileService;
import codehows.dream.dreambulider.service.BoardService;
import codehows.dream.dreambulider.service.HashTagService;
import codehows.dream.dreambulider.service.LikedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final HashTagService hashTagService;
    private final HashTagRepository hashTagRepository;
    private final LikedService likedService;
    private final MemberRepository memberRepository;
    private final BoardFileService boardFileService;
    //게시글 작성
    @PostMapping("/api/addBoard")
    public ResponseEntity<Board> addBoard(@RequestPart(name = "board") BoardRequestDTO boardDTO,
                                          @RequestPart(name = "files", required = false) List<MultipartFile> multipartFile) {
        try{
            Board savedBoard = boardService.save(boardDTO);
            boardDTO.getHashTags().forEach(e -> {
                hashTagService.saveHashTag(savedBoard.getId(),e);
            });
            // 첨부파일로 들어온 파일 처리하는 로직
            boardFileService.saveFiles(multipartFile);

//      위랑 같은거임.
//            for(HashTag hash : boardDTO.getHashTags()){
//                hashTagService.saveHashTag(savedBoard.getId(), hash);
//            }
        } catch (MemberNoExistExcpetion e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //201 완료 ok
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/upload")
    public ResponseEntity<?> addFiles(@RequestPart(name = "file") MultipartFile file) {
        Map<String,String> result = new HashMap<>();
        try {
            String fileUrl = boardFileService.saveFile(file);
            result.put("fileUrl",fileUrl);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(result);
    }

    //게시글 목록 조회
    @GetMapping("/api/boardList/{page}")
    public ResponseEntity<?> findAllBoardList(@PathVariable Optional<Integer> page, @RequestParam String search, @RequestParam String keyword) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Board> boardList;
        if(!search.isEmpty() && !keyword.isEmpty()) {
            boardList = boardService.searchBoard(pageable, search, keyword);
        } else {
            boardList = boardService.findAll(pageable);
        }

        //Member member = memberRepository.findById(memberId);

        List<BoardListResponseDTO> list = new ArrayList<>();
        for (Board board : boardList) {
            BoardListResponseDTO listResponseDTO = new BoardListResponseDTO();
            listResponseDTO.setId(board.getId());
            listResponseDTO.setTitle(board.getTitle());
            listResponseDTO.setEndDate(board.getEndDate());
            listResponseDTO.setHashTags(hashTagService.findAll(board.getId()));
            listResponseDTO.setLikeList(likedService.LikeList(board.getId()));
            listResponseDTO.setCountLike(likedService.countLike(board.getId()));
            list.add(listResponseDTO);
        }
        return ResponseEntity.ok().body(list);
   }
//
//    //게시글 목록 조회
@GetMapping("/api/boards/{page}")
public ResponseEntity<?> findBoardList(@PathVariable Optional<Integer> page) {
    Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

    Page<Board> boardList = boardService.findAll(pageable);

    //Member member = memberRepository.findById(memberId);

    List<BoardListResponseDTO> list = new ArrayList<>();
    for (Board board : boardList) {
        BoardListResponseDTO listResponseDTO = new BoardListResponseDTO();
        listResponseDTO.setId(board.getId());
        listResponseDTO.setTitle(board.getTitle());
        listResponseDTO.setEndDate(board.getEndDate());
        listResponseDTO.setHashTags(hashTagService.findAll(board.getId()));
        listResponseDTO.setLikeList(likedService.LikeList(board.getId()));
        listResponseDTO.setCountLike(likedService.countLike(board.getId()));
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
    public ResponseEntity<BoardDTO> updateBoard(@PathVariable long id, @RequestBody BoardUpdateDTO requestBoard) {
            Board board = boardService.update(id, requestBoard);
            hashTagService.updateHash(id, requestBoard.getHashTags());

            List<String> hashTags = hashTagRepository.findByBoardId(board.getId());

            BoardDTO boardto = new BoardDTO(board.getTitle(), board.getContent(), board.getEndDate(), hashTags);
        return ResponseEntity.ok()
                .body(boardto);
    }

    //비활성화
    @PutMapping("/api/{id}")
    public ResponseEntity<Board> deleteBoard(@PathVariable long id) {
        Board updatedBoard = boardService.updateInvisible(id);

        return ResponseEntity.ok()
                .body(updatedBoard);
    }

}
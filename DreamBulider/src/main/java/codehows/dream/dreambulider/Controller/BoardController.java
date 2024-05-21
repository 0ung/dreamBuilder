package codehows.dream.dreambulider.controller;

import codehows.dream.dreambulider.dto.Board.*;
import codehows.dream.dreambulider.dto.HashTag.MemberNoExistExcpetion;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.repository.HashTagRepository;
import codehows.dream.dreambulider.service.BoardFileService;
import codehows.dream.dreambulider.service.BoardService;
import codehows.dream.dreambulider.service.HashTagService;
import codehows.dream.dreambulider.service.LikedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

	private final BoardService boardService;
	private final HashTagService hashTagService;
	private final HashTagRepository hashTagRepository;
	private final LikedService likedService;
	private final BoardFileService boardFileService;

	//게시글 작성
	@PostMapping("/api/addBoard")
	public ResponseEntity<Board> addBoard(
			@RequestPart(name = "board") BoardRequestDTO boardDTO,
			@RequestPart(name = "files", required = false) List<MultipartFile> multipartFile,
			Principal principal) {
		try {
			boardDTO.replaceTempUrlsWithPermanentUrls(boardFileService);
			Board savedBoard = boardService.save(boardDTO, principal.getName());
			boardDTO.getHashTags().forEach(e -> {
				hashTagService.saveHashTag(savedBoard.getId(), e);
			});
			// 첨부파일로 들어온 파일 처리하는 로직
			boardFileService.saveFiles(multipartFile, savedBoard.getId());

		} catch (MemberNoExistExcpetion e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		//201 완료 ok
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	//파일 업로드
	@PostMapping("/api/upload")
	public ResponseEntity<?> addFiles(@RequestPart(name = "file") MultipartFile file) {
		Map<String, String> result = new HashMap<>();
		try {
			String fileUrl = boardFileService.saveTempFile(file);
			result.put("fileUrl", fileUrl);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(result);
	}

	//게시글 검색 and 목록 조회
	@GetMapping("/api/boards/{page}")
	public ResponseEntity<?> searchBoardList(@PathVariable Optional<Integer> page,
		@RequestParam(required = false, name = "search") String search,
		@RequestParam(required = false, name = "criteria") String criteria,
		@RequestParam(required = false) String sort, Principal principal
	) {
		try {
			int currentPage = page.orElse(0);
			Pageable pageable = boardService.sorted(sort, currentPage);
			List<BoardListResponseDTO> boardPage = boardService.searchBoard(pageable, criteria, search, principal);
			return new ResponseEntity<>(boardPage, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	//게시글 상세 조회
	@GetMapping("/api/board/{id}")
	public ResponseEntity<BoardResponseDTO> findBoard(@PathVariable long id) {
		Board board = boardService.findById(id);
		board.setCnt(boardService.incrementAndViewCnt(id)); //조회수
		List<String> hashTags = hashTagService.findById(id);
		List<Map<String, String>> file = boardFileService.findBoardUrl(id);
		BoardResponseDTO dt = new BoardResponseDTO(board, hashTags, file);
		return ResponseEntity.ok()
			.body(dt);
	}

	//게시글 수정
	@PutMapping("/api/board/{id}")
	public ResponseEntity<BoardDTO> updateBoard(@PathVariable long id, @RequestBody BoardUpdateDTO requestBoard, Principal principal) {
		Board board = boardService.update(id, requestBoard, principal);
		hashTagService.updateHash(id, requestBoard.getHashTags());

		List<String> hashTags = hashTagRepository.findByBoardId(board.getId());

		BoardDTO boardto = new BoardDTO(board.getTitle(), board.getContent(), board.getEndDate(), hashTags);
		return ResponseEntity.ok()
			.body(boardto);
	}

	//비활성화
	@PutMapping("/api/{id}")
	public ResponseEntity<Board> deleteBoard(@PathVariable long id, Principal principal) {
		Board updatedBoard = boardService.updateInvisible(id, principal);

		return ResponseEntity.ok()
			.body(updatedBoard);
	}

}
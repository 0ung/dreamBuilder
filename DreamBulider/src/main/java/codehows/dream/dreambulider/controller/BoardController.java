package codehows.dream.dreambulider.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import codehows.dream.dreambulider.dto.Board.BoardDTO;
import codehows.dream.dreambulider.dto.Board.BoardRequestDTO;
import codehows.dream.dreambulider.dto.Board.BoardResponseDTO;
import codehows.dream.dreambulider.dto.Board.BoardUpdateDTO;
import codehows.dream.dreambulider.dto.HashTag.MemberNoExistExcpetion;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.repository.HashTagRepository;
import codehows.dream.dreambulider.service.BoardFileService;
import codehows.dream.dreambulider.service.BoardService;
import codehows.dream.dreambulider.service.HashTagService;
import codehows.dream.dreambulider.service.LikedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	public ResponseEntity<Board> addBoard(@RequestPart(name = "board") BoardRequestDTO boardDTO,
		@RequestPart(name = "files", required = false) List<MultipartFile> multipartFile) {
		try {
			boardDTO.replaceTempUrlsWithPermanentUrls(boardFileService);
			Board savedBoard = boardService.save(boardDTO);
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

	//게시글 검색
	@GetMapping("/api/boards/{page}")
	public ResponseEntity<?> searchBoardList(@PathVariable Optional<Integer> page,
		@RequestParam(name = "search") String search,
		@RequestParam(name = "criteria") String criteria,
		@RequestParam(required = false) String sort
	) {
		try {
			int currentPage = page.orElse(0);
			Pageable pageable = boardService.sorted(sort, currentPage);
			Page<Board> boardPage = boardService.searchBoard(pageable, criteria, search);

			return new ResponseEntity<>(boardPage, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	//게시글 목록 조회


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
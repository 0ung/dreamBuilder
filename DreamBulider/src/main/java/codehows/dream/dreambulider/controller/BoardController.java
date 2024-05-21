package codehows.dream.dreambulider.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import codehows.dream.dreambulider.entity.Member;
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
import codehows.dream.dreambulider.dto.Board.BoardListResponseDTO;
import codehows.dream.dreambulider.dto.Board.BoardRequestDTO;
import codehows.dream.dreambulider.dto.Board.BoardResponseDTO;
import codehows.dream.dreambulider.dto.Board.BoardUpdateDTO;
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
		@RequestPart(name = "files", required = false) List<MultipartFile> multipartFile, Principal principal) {
		try {
			Board savedBoard = boardService.save(boardDTO);
			boardDTO.getHashTags().forEach(e -> {
				hashTagService.saveHashTag(savedBoard.getId(), e);
			});
			// 첨부파일로 들어온 파일 처리하는 로직
			boardFileService.saveFiles(multipartFile, savedBoard.getId());


			//      위랑 같은거임.
			//            for(HashTag hash : boardDTO.getHashTags()){
			//                hashTagService.saveHashTag(savedBoard.getId(), hash);
			//            }
		} catch (MemberNoExistExcpetion e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		//201 완료 ok
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/api/upload")
	public ResponseEntity<?> addFiles(@RequestPart(name = "file") MultipartFile file,
		@RequestPart(name = "boardId") Long boardId) {
		Map<String, String> result = new HashMap<>();
		try {
			String fileUrl = boardFileService.saveFile(file, boardId);
			result.put("fileUrl", fileUrl);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(result);
	}

	//게시글 목록 조회
	@GetMapping("/api/boards/{page}")
	public ResponseEntity<?> findBoardList(@PathVariable Optional<Integer> page,
		@RequestParam(required = false) String search,
		@RequestParam(required = false) String keyword, @RequestParam(required = false) String sort, Principal principal) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

		int currentPage = page.orElse(0);
		//정렬
		Sort.Direction sortDirection = Sort.Direction.DESC;
		if (sort != null) {
			switch (sort) {
				case "title":
				case "content":
				case "endDate":
					pageable = PageRequest.of(currentPage, 10, Sort.by(sortDirection, sort));
					break;
				default:
					pageable = PageRequest.of(currentPage, 10, Sort.by(Sort.Direction.DESC, "id"));
					break;
			}
		} else {
			pageable = PageRequest.of(currentPage, 10, Sort.by(Sort.Direction.DESC, "id"));
		}
		//검색
		Page<Board> boardList;
		if (search != null && keyword != null) {
			boardList = boardService.searchBoard(pageable, search, keyword);
		} else {
			boardList = boardService.findAll(pageable);
		}

		//Member member = memberRepository.findById(memberId);

		// 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
		LocalDate now = LocalDate.now();

		List<BoardListResponseDTO> list = new ArrayList<>();
		for (Board board : boardList) {
			BoardListResponseDTO listResponseDTO = new BoardListResponseDTO();
			listResponseDTO.setId(board.getId());
			listResponseDTO.setTitle(board.getTitle());
			listResponseDTO.setEndDate(board.getEndDate());
			listResponseDTO.setHashTags(hashTagService.findAll(board.getId()));
			listResponseDTO.setCnt(boardService.getCnt(board.getId()));
			if ((now.isAfter(board.getEndDate().toLocalDate()))) {
				listResponseDTO.setDeadLine(true);
			} else {
				listResponseDTO.setDeadLine(false);
			}
			if(principal.getName()!=null) {
				listResponseDTO.setLikeList(likedService.LikeList(board.getId(), principal));
			} else {
				listResponseDTO.setLikeList(null);
			}
			listResponseDTO.setCountLike(likedService.countLike(board.getId()));
			list.add(listResponseDTO);
		}
		return ResponseEntity.ok().body(list);
	}

	//게시글 상세 조회
	@GetMapping("/api/board/{id}")
	public ResponseEntity<BoardResponseDTO> findBoard(@PathVariable long id) {
		Board board = boardService.findById(id);
		board.setCnt(boardService.incrementAndViewCnt(id)); //조회수
		List<String> hashTags = hashTagService.findById(id);
		List<String> file = boardFileService.findBoardUrl(id);
		BoardResponseDTO dt = new BoardResponseDTO(board, hashTags,file);
		return ResponseEntity.ok()
			.body(dt);
	}

	//게시글 수정
	@PutMapping("/api/board/{id}")
	public ResponseEntity<BoardDTO> updateBoard(@PathVariable long id, @RequestBody BoardUpdateDTO requestBoard, Principal principal) {

		BoardDTO boardto;

		try {
			Board board = boardService.update(id, requestBoard, principal);
			hashTagService.updateHash(id, requestBoard.getHashTags());

			List<String> hashTags = hashTagRepository.findByBoardId(board.getId());
			boardto = new BoardDTO(board.getTitle(), board.getContent(), board.getEndDate(), hashTags);
		} catch (SecurityException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
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
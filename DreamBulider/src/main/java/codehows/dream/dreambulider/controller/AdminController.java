package codehows.dream.dreambulider.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import codehows.dream.dreambulider.dto.Board.BoardAdminListDTO;
import codehows.dream.dreambulider.dto.Board.FileDTO;
import codehows.dream.dreambulider.entity.FileManage;
import codehows.dream.dreambulider.service.BoardFileService;
import codehows.dream.dreambulider.service.BoardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminController {

	private final BoardService boardService;
	private final BoardFileService boardFileService;

	//관리자 게시물 조회 페이지
	@GetMapping("/api/admin/{page}")
	public ResponseEntity<List<BoardAdminListDTO>> getAdminBoard(@PathVariable Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
		List<BoardAdminListDTO> board = boardService.getAdminBoardList(pageable)
			.stream()
			.map(BoardAdminListDTO::new)
			.toList();
		return ResponseEntity.ok()
			.body(board);
	}

	//관리자 : 게시글 복구하기
	@PutMapping("/api/admin/{id}")
	public ResponseEntity<?> visible(@PathVariable long id) {
		boardService.visibleUp(id);

		return new ResponseEntity<>(HttpStatus.OK);
		// return ResponseEntity.ok()
		//       .body(updatedBoard);
	}

	//관리자 : 게시글 삭제하기
	@PutMapping("/api/admin/del/{id}")
	public ResponseEntity<?> invisible(@PathVariable long id) {
		boardService.invisible(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	//관리자 : 전체 페이지 카운트
	@GetMapping("/api/admin/total")
	public ResponseEntity<?> getTotal() {
		return new ResponseEntity<>(boardService.getTotalCnt(), HttpStatus.OK);
	}

	//관리자 파일 설정
	@PostMapping("/api/admin/file")
	public ResponseEntity<?> fileAdmin(@RequestBody FileDTO dto) {
		boardFileService.fileAdmin(dto);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	//관리자 파일 불러오기
	@GetMapping("/api/admin/file")
	public ResponseEntity<FileManage> fileResponse() {
		FileManage file = boardFileService.fileResponse();

		return ResponseEntity.ok()
			.body(file);
	}

}

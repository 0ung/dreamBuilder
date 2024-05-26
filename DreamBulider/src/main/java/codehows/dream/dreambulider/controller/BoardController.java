package codehows.dream.dreambulider.controller;

import codehows.dream.dreambulider.dto.Board.BoardListResponseDTO;
import codehows.dream.dreambulider.dto.Board.BoardRequestDTO;
import codehows.dream.dreambulider.dto.Board.BoardResponseDTO;
import codehows.dream.dreambulider.dto.HashTag.MemberNoExistExcpetion;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.service.BoardFileService;
import codehows.dream.dreambulider.service.BoardService;
import codehows.dream.dreambulider.service.HashTagService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
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
	private final BoardFileService boardFileService;
	@Value("${savePath}")
	private String savePath;
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
		@RequestParam(required = false, name = "sort") String sort, Principal principal
	) {
		try {
			int currentPage = page.orElse(0);
			Pageable pageable = boardService.sorted(sort, currentPage);
			List<BoardListResponseDTO> boardPage = boardService.searchBoard(pageable, criteria, search, principal);
			return new ResponseEntity<>(boardPage, HttpStatus.OK);
		} catch (NullPointerException e) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
	public ResponseEntity<?> updateBoard(@PathVariable long id,
		@RequestPart(name = "board") BoardRequestDTO boardDTO,
		@RequestPart(name = "files", required = false) List<MultipartFile> multipartFile,
		Principal principal) {
		try {
			boardService.update(id, boardDTO, principal);
			hashTagService.updateHash(id, boardDTO.getHashTags());
			boardFileService.updateFile(id, multipartFile);
			return new ResponseEntity<>("업데이트 완료", HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("업데이트 실패", HttpStatus.BAD_REQUEST);
		}
	}

	//비활성화
	@DeleteMapping("/api/{id}")
	public ResponseEntity<?> deleteBoard(@PathVariable long id, Principal principal) {
		try {
			boardService.updateInvisible(id, principal);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/download/files/{fileName}/{name}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,@PathVariable String name) throws IOException {
		File file = new File(savePath + fileName);

		if (!file.exists()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

		return ResponseEntity.ok()
			.headers(headers)
			.contentLength(file.length())
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.body(resource);
	}

	//엑셀 다운로드
	@GetMapping("/excel/download")
	public void excelDownload(HttpServletResponse response) throws IOException {

		List<Board> boardList = boardService.excelBoardList();

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("first board sheet");

		Row row = null;
		Cell cell = null;
		int rowNum = 0;
		String[] rowData =new String[]{"번호","제목","내용","조회수","마감일자","해시태그"};

		row = sheet.createRow(rowNum++);
		for (int i = 0; i < rowData.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(rowData[i]);
		}

		for(Board board : boardList) {
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0);
			cell.setCellValue(board.getId());
			cell = row.createCell(1);
			cell.setCellValue(board.getTitle());
			cell = row.createCell(2);
			cell.setCellValue(board.getContent());
			cell = row.createCell(3);
			cell.setCellValue(board.getCnt());
			cell = row.createCell(4);
			cell.setCellValue(board.getEndDate());
			cell = row.createCell(5);
			List<String> hashTagList = hashTagService.findAll(board.getId());
			cell.setCellValue(hashTagList.toString());
		}

		// 컨텐츠 타입과 파일명 지정
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

		// Excel File Output
		workbook.write(response.getOutputStream());
		workbook.close();
	}

	//상위 5개 출력
	@GetMapping("/api/main")
	public ResponseEntity<List<BoardResponseDTO>> topBoard() {
		List<BoardResponseDTO> board = boardService.topBoard()
				.stream()
				.map(BoardResponseDTO::new)
				.toList();
		log.info(board.toString());
		return ResponseEntity.ok()
				.body(board);
	}

}
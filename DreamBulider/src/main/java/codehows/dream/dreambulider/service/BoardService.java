package codehows.dream.dreambulider.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codehows.dream.dreambulider.dto.Board.BoardListResponseDTO;
import codehows.dream.dreambulider.dto.Board.BoardRequestDTO;
import codehows.dream.dreambulider.dto.Board.BoardUpdateDTO;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.repository.BoardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	private final HashTagService hashTagService;
	private final LikedService likedService;

	public Board save(BoardRequestDTO boardDTO) {
		return boardRepository.save(Board.builder()
			.title(boardDTO.getTitle())
			.content(boardDTO.getContent())
			.endDate(boardDTO.getEndDate())
			.build());
	}

	public Page<Board> findAll(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	public Page<Board> searchBoard(Pageable pageable
		, String criteria
		, String keyword
	) {
		return switch (criteria.toLowerCase()) {
			case "title" -> boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
			case "content" -> boardRepository.findByContentContainingIgnoreCase(keyword, pageable);
			case "member" -> boardRepository.findByMemberContainingIgnoreCase(keyword, pageable);
			case "title,content" -> boardRepository.findByTitleOrContent(keyword, pageable);
			case "title,member" -> boardRepository.findByTitleOrAuthor(keyword, pageable);
			case "content,member" -> boardRepository.findByContentOrAuthor(keyword, pageable);
			case "title,content,member" -> boardRepository.findByTitleOrContentOrAuthor(keyword, pageable);
			default -> Page.empty(pageable);
		};
	}

	public Pageable sorted(String sort, int currentPage) {
		Sort.Direction sortDirection = Sort.Direction.DESC;
		Pageable pageable = null;
		if (sort == null) {
			sort = "";
		}
		pageable = switch (sort) {
			case "title" -> PageRequest.of(currentPage, 10, Sort.by(sortDirection, sort));
			case "content" -> PageRequest.of(currentPage, 10, Sort.by(sortDirection, sort));
			case "endDate" -> PageRequest.of(currentPage, 10, Sort.by(sortDirection, sort));
			default -> PageRequest.of(currentPage, 10, Sort.by(Sort.Direction.DESC, "id"));
		};
		return pageable;
	}

	//게시글 상세 조회
	public Board findById(long id) {
		return boardRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("not found: " + id));
	}

	//조회수 +1
	@Transactional
	public Long incrementAndViewCnt(long id) {
		boardRepository.incrementCnt(id);
		Long getCnt = boardRepository.getCntById(id);
		return getCnt;
		//return (getCnt != null) ? getCnt : 1L;
	}

	public Long getCnt(long id) {
		return boardRepository.getCntById(id);
	}

	@Transactional
	public Board update(long id, BoardUpdateDTO request) {
		Board board = boardRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("not found : " + id));

		board.update(request.getTitle(), request.getContent(), request.getEndDate());

		return board;
	}

	//    @Transactional
	//    public BoardUpdateDTO updateDTO(Long boardId) {
	//
	//        List<HashTag> hashTagList = hashTagRepository.findByBoardId(boardId);
	//        List<BoardUpdateDTO> boardDtoList = new ArrayList<>();
	//        for (HashTag hashTag : hashTagList) {
	//            BoardUpdateDTO boardDto = BoardUpdateDTO.of(hashTag);
	//            boardDtoList.add(boardDto);
	//        }
	//    }

	//비활성화(삭제)
	@Transactional
	public Board updateInvisible(long id) {
		Board board = boardRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("not found:" + id));
		board.update1();

		return board;
	}

	public List<BoardListResponseDTO> boardToList(Page<Board> boardList) {
		List<BoardListResponseDTO> list = new ArrayList<>();
		for (Board board : boardList) {
			BoardListResponseDTO listResponseDTO = new BoardListResponseDTO();
			listResponseDTO.setId(board.getId());
			listResponseDTO.setTitle(board.getTitle());
			listResponseDTO.setEndDate(board.getEndDate());
			listResponseDTO.setHashTags(hashTagService.findAll(board.getId()));
			listResponseDTO.setCnt(getCnt(board.getId()));
			listResponseDTO.setLikeList(likedService.LikeList(board.getId()));
			listResponseDTO.setCountLike(likedService.countLike(board.getId()));
			list.add(listResponseDTO);
		}
		return list;
	}
}

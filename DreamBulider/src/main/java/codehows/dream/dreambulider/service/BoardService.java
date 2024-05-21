package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.dto.Board.BoardListResponseDTO;
import codehows.dream.dreambulider.dto.Board.BoardRequestDTO;
import codehows.dream.dreambulider.dto.Board.BoardUpdateDTO;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.HashTagRepository;
import codehows.dream.dreambulider.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	private final HashTagService hashTagService;
	private final LikedService likedService;
	private final MemberRepository memberRepository;
	private final HashTagRepository hashTagRepository;

	public Board save(BoardRequestDTO boardDTO, String email) {
		return boardRepository.save(Board.builder()
			.title(boardDTO.getTitle())
			.content(boardDTO.getContent())
			.endDate(boardDTO.getEndDate())
			.member(memberRepository.findMemberByEmail(email).orElse(null))
			.build());
	}

	public List<BoardListResponseDTO> searchBoard(Pageable pageable
		, String criteria
		, String keyword, Principal principal
	) {
		Page<Board> boards = null;
		String escapedKeyword = keyword.replace("\\", "\\\\");
		switch (criteria.toLowerCase()) {
			case "title" -> boards = boardRepository.findByTitleContainingIgnoreCase(escapedKeyword, pageable);
			case "content" -> boards = boardRepository.findByContentContainingIgnoreCase(escapedKeyword, pageable);
			case "member" -> boards = boardRepository.findByMemberContainingIgnoreCase(escapedKeyword, pageable);
			case "title,content" -> boards = boardRepository.findByTitleOrContent(escapedKeyword, pageable);
			case "title,member" -> boards = boardRepository.findByTitleOrAuthor(escapedKeyword, pageable);
			case "content,member" -> boards = boardRepository.findByContentOrAuthor(escapedKeyword, pageable);
            case "hashtag" -> {
                for(Long boardId : hashTagRepository.findHashTagByHashTag(escapedKeyword)) {
                    boards = boardRepository.findByboardId(boardId, pageable);
                }
            }
            case "title,content,member" ->
					boards = boardRepository.findByTitleOrContentOrAuthor(escapedKeyword, pageable);
			default -> boards = boardRepository.findAll(pageable);
		};
		return boardToList(boards, principal);
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
    //조회수 조회
    public Long getCnt(long id) {
        return boardRepository.getCntById(id);
    }

    //게시글 업데이트
    @Transactional
    public Board update(long boardId, BoardUpdateDTO request, Principal principal) {
        Member member = boardRepository.findMemberByBoardId(boardId);

        if (member.getEmail() == null) {
            throw new IllegalArgumentException("Invalid board Id:" + boardId);
        }
        if(!principal.getName().equals(member.getEmail())) {
            throw new SecurityException("You do not have permission to edit this board");
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + boardId));
        board.update(request.getTitle(), request.getContent(), request.getEndDate());
        return board;
    }

    //비활성화(삭제)
    @Transactional
    public Board updateInvisible(long boardId, Principal principal) {
        Member member = boardRepository.findMemberByBoardId(boardId);

        if (member.getEmail() == null) {
            throw new IllegalArgumentException("Invalid board Id:" + boardId);
        }
        if(!principal.getName().equals(member.getEmail())) {
            throw new SecurityException("You do not have permission to edit this board");
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + boardId));
        board.update1();
		return board;
	}

	public List<BoardListResponseDTO> boardToList(Page<Board> boardList, Principal principal) {
		List<BoardListResponseDTO> list = new ArrayList<>();

		for (Board board : boardList) {
			BoardListResponseDTO listResponseDTO = new BoardListResponseDTO();
			listResponseDTO.setId(board.getId());
			listResponseDTO.setTitle(board.getTitle());
			listResponseDTO.setEndDate(board.getEndDate());
			listResponseDTO.setHashTags(hashTagService.findAll(board.getId()));
			listResponseDTO.setCnt(getCnt(board.getId()));
			listResponseDTO.setLikeList(likedService.LikeList(board.getId(), principal));
			listResponseDTO.setCountLike(likedService.countLike(board.getId()));
			list.add(listResponseDTO);
		}

		return list; // 새로운 Page 객체 생성
	}
}

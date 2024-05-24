package codehows.dream.dreambulider.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import codehows.dream.dreambulider.constats.Authority;
import codehows.dream.dreambulider.dto.Board.*;
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

import codehows.dream.dreambulider.dto.Board.BoardListResponseDTO;
import codehows.dream.dreambulider.dto.Board.BoardRequestDTO;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	private final HashTagService hashTagService;
	private final LikedService likedService;
	private final MemberRepository memberRepository;
	private final HashTagRepository hashTagRepository;
	private final ReplyService replyService;
	private final BoardFileService boardFileService;

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
			case "title,content,member" ->
				boards = boardRepository.findByTitleOrContentOrAuthor(escapedKeyword, pageable);
			case "hashtag" -> {
				for (Long boardId : hashTagRepository.findHashTagByHashTag(escapedKeyword)) {
					boards = boardRepository.findByboardId(boardId, pageable);
				}
			}
			default -> boards = boardRepository.findAll(pageable);
		}
		;
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

	//관리자 게시물 조회 페이지
	public Page<Board> getAdminBoardList(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	//게시물 엑셀 조회 페이지
	public List<Board> excelBoardList() {
		List<Board> boardList = boardRepository.findAll();

		return boardList;
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
	public Board update(long boardId, BoardRequestDTO request, Principal principal) {
		Member member = boardRepository.findMemberByBoardId(boardId);
		if (member.getEmail() == null) {
			throw new IllegalArgumentException("Invalid board Id:" + boardId);
		}
		if (!principal.getName().equals(member.getEmail())) {
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
		if (!principal.getName().equals(member.getEmail())) {
			throw new SecurityException("You do not have permission to edit this board");
		}

		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("not found:" + boardId));
		board.updateInvisible();
		return board;
	}

	//리스트 만들기
	public List<BoardListResponseDTO> boardToList(Page<Board> boardList, Principal principal) {
		List<BoardListResponseDTO> list = new ArrayList<>();

		for (Board board : boardList) {
			BoardListResponseDTO listResponseDTO = new BoardListResponseDTO();
			listResponseDTO.setId(board.getId());
			listResponseDTO.setTitle(board.getTitle());
			listResponseDTO.setEndDate(board.getEndDate());
			listResponseDTO.setHashTags(hashTagService.findAll(board.getId()));
			listResponseDTO.setCnt(getCnt(board.getId()));
			listResponseDTO.setCountLike(likedService.countLike(board.getId()));
			listResponseDTO.setReplyCnt(replyService.getReplyCnt(board.getId()));
			if (principal != null) {
				listResponseDTO.setLikeList(likedService.LikeList(board.getId(), principal));
			}

			list.add(listResponseDTO);
		}

		return list;
	}

	//관리자 게시글 삭제
	@Transactional
	public void invisible(long boardId) {
		Board board = boardRepository.findById(boardId)
				.orElseThrow(() -> new IllegalArgumentException("not found:" + boardId));
		board.setInvisible(true);
		board.setDeleteBy(Authority.ROLE_USER);
		boardRepository.save(board);
	}

	//관리자 게시글 복구
	@Transactional
	public void visibleUp(long boardId) {
		Board board = boardRepository.findById(boardId)
				.orElseThrow(() -> new IllegalArgumentException("not found:" + boardId));
		board.setInvisible(false);
		board.setDeleteBy(Authority.ROLE_ADMIN);
		boardRepository.save(board);
	}

	//마이페이지 작성 글 찾기
	public Page<Board> myBoard(Principal principal, Pageable pageable) {
		Member member = memberRepository.findMemberByEmail(principal.getName())
				.orElseThrow(() -> new IllegalArgumentException("not found member" ));

		return boardRepository.findBoardByMemberId(member.getId(), pageable);
	}

	//이번 달 작성한 글 개수
	public Long countBoard(Principal principal) {
		Member member = memberRepository.findMemberByEmail(principal.getName())
				.orElseThrow(() -> new IllegalArgumentException("not found member" ));

		Long count = boardRepository.countBoardByMember(member.getId());

		return count;
	}

	//사용자 전체 프로젝트 조회
	public Long getTotalCnt(){
		return boardRepository.count();
	}
}

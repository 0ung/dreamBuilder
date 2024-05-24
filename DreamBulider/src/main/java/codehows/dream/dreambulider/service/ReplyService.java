package codehows.dream.dreambulider.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedResponseDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyDeleteDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyRequestDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyResponseDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyUpdateDTO;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.entity.NestedReply;
import codehows.dream.dreambulider.entity.Reply;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.MemberRepository;
import codehows.dream.dreambulider.repository.NestedRepository;
import codehows.dream.dreambulider.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {

	private final ReplyRepository replyRepository;
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;
	private final NestedRepository nestedRepository;

	public Reply saveReply(ReplyRequestDTO replyRequestDTO, String email) {

		Member member = memberRepository.findMemberByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));
		Board board = boardRepository.findById(replyRequestDTO.getBoardId())
			.orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

		if (replyRequestDTO.getComment() == null) {
			throw new IllegalArgumentException("데이터 없음");
		}

		Reply result = Reply.builder()
			.comment(replyRequestDTO.getComment())
			.invisible(replyRequestDTO.isInvisible())
			.board(board)
			.member(member)
			.build();
		replyRepository.save(result);

		return result;
	}

	public Page<Reply> findAll(Long boardId, Pageable pageable) {
		return replyRepository.findByBoardId(boardId
			, pageable);
	}

	public int getTotal(Long boardId) {
		Pageable pageable = PageRequest.of(0, 5);
		return replyRepository.findByBoardId(boardId, pageable).getTotalPages();
	}

	public ReplyResponseDTO findById(long id) {
		Reply reply = replyRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("not found : " + id));
		return new ReplyResponseDTO(reply);
	}

	@Transactional
	public ReplyDeleteDTO deleteInvisible(long id) {
		Reply reply = replyRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("not found : " + id));
		reply.delete();
		replyRepository.save(reply);
		return new ReplyDeleteDTO(reply);
	}

	@Transactional
	public Reply update(long id, ReplyUpdateDTO replyUpdateDTO) {
		Reply reply = replyRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("not found : " + id));

		reply.update(replyUpdateDTO.getComment());

		return reply;
	}

	public Long getReplyCnt(long boardId) {
		return replyRepository.countReplyByBoardId(boardId);
	}

	//이번 달 작성한 댓글 개수
	public Long countReply(Principal principal) {
		Member member = memberRepository.findMemberByEmail(principal.getName())
			.orElseThrow(() -> new IllegalArgumentException("not found member"));

		Long count = replyRepository.countReplyByMember(member.getId());

		return count;
	}

	//전체 댓글 개수
	public List<ReplyResponseDTO> getAdminData(Pageable pageable) {
		List<ReplyResponseDTO> result = new ArrayList<>();
		Page<Reply> replies = replyRepository.findAllBy(pageable);
		replies.forEach((e) -> {
			List<NestedReply> nestedReplies = nestedRepository.findByReplyIdOrderByIdDesc(e.getId());
			List<NestedResponseDTO> list = nestedReplies.stream().map(NestedResponseDTO::new).toList();
			result.add(ReplyResponseDTO.entityToDTO(e, list));
		});
		return result;
	}
}

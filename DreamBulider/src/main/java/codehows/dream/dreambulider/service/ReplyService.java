package codehows.dream.dreambulider.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import codehows.dream.dreambulider.dto.ReplyDTO.ReplyDeleteDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyRequestDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyResponseDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyUpdateDTO;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.entity.Reply;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.MemberRepository;
import codehows.dream.dreambulider.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {

	private final ReplyRepository replyRepository;
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;

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
			.createdDate(replyRequestDTO.getRegDate())
			.build();
		replyRepository.save(result);

		return result;
	}

	public Page<Reply> findAll(Long boardId, Pageable pageable) {
		return replyRepository.findByBoard(boardRepository.findById(boardId).orElse(null)
			, pageable);
	}

	/*public Reply findById(long id) {
		return replyRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("not found : " + id));
	}*/
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
}

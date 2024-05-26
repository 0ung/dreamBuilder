package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.dto.Liked.LikedRequest;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.Liked;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.LikedRepository;
import codehows.dream.dreambulider.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Transactional
@RequiredArgsConstructor
public class LikedService {
	private final LikedRepository likedRepository;
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;

	//좋아요 추가/삭제
	public void insert(LikedRequest likedRequest, Principal principal) {


		Board board = boardRepository.findById(likedRequest.getBoardId())
			.orElseThrow(() -> new IllegalArgumentException("not found:" + likedRequest.getBoardId()));

		Member member = memberRepository.findMemberByEmail(principal.getName())
			.orElseThrow(() -> new IllegalArgumentException("not found:" + principal.getName()));

		Boolean like = likedRepository.findIsLikeByBoardIdAndMemberId(likedRequest.getBoardId(), member.getId());

		//좋아요가 처음이면 db 생성
		if (likedRepository.findIsLikeByBoardIdAndMemberId(likedRequest.getBoardId(), member.getId()) == null) {
			likedRepository.save(Liked.builder()
				.board(board)
				.member(member)
				.isLike(true)
				.build());
			//좋아요가 있다면 취소
            return ;
		}
        Liked liked = likedRepository.findAllByBoardIdAndMemberId(board.getId(), member.getId());
		liked.update(!like);
	}

	//좋아요 개수 출력
	public Long countLike(long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("not found:" + boardId));

		Long count = likedRepository.countByBoardId(board.getId());

		return count;
	}

	//좋아요 상태 리스트 출력
	//좋아요 상태 리스트 출력
	public Boolean LikeList(long boardId, Principal principal) {

		Board board = boardRepository.findById(boardId) //해당 게시글 찾고
			.orElseThrow(() -> new IllegalArgumentException("not found:" + boardId));
		Member member = memberRepository.findMemberByEmail(principal.getName())
			.orElseThrow(() -> new IllegalArgumentException("not found member"));

		Boolean like = likedRepository.findByBoardIdAndMemberId(board.getId(), member.getId());
		return like;

	}

    //이번 달 받은 좋아요 개수
    public Long likedCount(Principal principal) {

        Member member = memberRepository.findMemberByEmail(principal.getName()) //principal 이메일로 member를 먼저 찾고
                .orElseThrow(() -> new IllegalArgumentException("not found member"));

        Long count = likedRepository.countByMemberId(member.getId()); //memberId로 count 찾기

        return count;
    }



}

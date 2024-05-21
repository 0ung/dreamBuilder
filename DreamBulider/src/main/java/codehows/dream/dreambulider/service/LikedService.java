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
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikedService {
    private final LikedRepository likedRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

//좋아요 추가/삭제
    public void insert(LikedRequest likedRequest) {

        Board board = boardRepository.findById(likedRequest.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("not found:" + likedRequest.getBoardId()));

        Member member = memberRepository.findById(likedRequest.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("not found:" + likedRequest.getMemberId()));

        Boolean like = likedRepository.findIsLikeByBoardIdAndMemberId(likedRequest.getBoardId(), likedRequest.getMemberId());

        //좋아요가 처음이면 db 생성
        if(likedRepository.findIsLikeByBoardIdAndMemberId(likedRequest.getBoardId(), likedRequest.getMemberId())==null) {
            likedRepository.save(Liked.builder()
                    .board(board)
                    .member(member)
                    .isLike(true)
                    .build());
            //좋아요가 있다면 취소
        } else if (likedRepository.findIsLikeByBoardIdAndMemberId(likedRequest.getBoardId(), likedRequest.getMemberId())!=null) {
            Liked liked = likedRepository.findAllByBoardIdAndMemberId(board.getId(), member.getId());
            liked.update(false);
            //likedRepository.save(liked);
        }
    }

    //좋아요 개수 출력
    public Long countLike(long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + boardId));

        Long count = likedRepository.countByBoardId(board.getId());

        return count;
    }

    //좋아요 상태 리스트 출력
    public List<Boolean> LikeList(long boardId, Principal principal) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + boardId));
        Member member = boardRepository.findMemberByBoardId(boardId);

        if (member.getEmail() == null) {
            throw new IllegalArgumentException("Invalid board Id:" + boardId);
        }
        if(!principal.getName().equals(member.getEmail())) {
            throw new SecurityException("You do not have permission to edit this board");
        }

        List<Boolean> like = likedRepository.findByBoardId(board.getId());


        return like;

    }



}

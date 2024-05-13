package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.dto.Board.BoardRequestDTO;
import codehows.dream.dreambulider.dto.Board.UpdateRequestBoard;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final HashTagRepository hashTagRepository;

    //게시글 작성
//    public Board save(AddRequestBoard addRequestBoard) {
//        return boardRepository.save(addRequestBoard.toEntity());
//    }
    public Board save(BoardRequestDTO boardDTO){
        return boardRepository.save(Board.builder()
                        .title(boardDTO.getTitle())
                        .content(boardDTO.getContent())
                        .endDate(boardDTO.getEndDate())
                .build());
    }
    //게시글 목록 조회
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    //게시글 상세 조회
    public Board findById(long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public List<HashTag> findHashTagsByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + boardId));
        return hashTagRepository.findByBoardId(board.getId());
    }

    //게시글 수정
    @Transactional
    public Board update(long id, UpdateRequestBoard requestBoard) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + id));
        board.update(requestBoard.getTitle(), requestBoard.getContent(), requestBoard.getEndDate());

        return board;
    }

    //비활성화(삭제)
    @Transactional
    public Board updateInvisible(long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + id));
        board.update1();

        return board;
    }
}

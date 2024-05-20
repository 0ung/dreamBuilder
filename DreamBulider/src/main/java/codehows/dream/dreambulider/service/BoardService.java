package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.dto.Board.BoardListResponseDTO;
import codehows.dream.dreambulider.dto.Board.BoardRequestDTO;
import codehows.dream.dreambulider.dto.Board.BoardUpdateDTO;
import codehows.dream.dreambulider.dto.Board.UpdateRequestBoard;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final HashTagRepository hashTagRepository;

    public Board save(BoardRequestDTO boardDTO){
        return boardRepository.save(Board.builder()
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .endDate(boardDTO.getEndDate())
                .build());
    }

    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> searchBoard(Pageable pageable, String search, String keyword) {
        if ("title".equalsIgnoreCase(search)) {
            return boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        } else if("content".equalsIgnoreCase(search)) {
            return boardRepository.findByContentContainingIgnoreCase(keyword, pageable);
        } else {
            return Page.empty(pageable);
        }
    }

    //게시글 상세 조회
    public Board findById(long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
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

}

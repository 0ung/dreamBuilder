package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.dto.Board.BoardRequestDTO;
import codehows.dream.dreambulider.dto.Board.BoardUpdateDTO;
import codehows.dream.dreambulider.dto.Board.UpdateRequestBoard;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.HashTag;
import codehows.dream.dreambulider.repository.BoardRepository;
import codehows.dream.dreambulider.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;
    private final BoardRepository boardRepository;

    //해시태그 저장
    @Transactional
    public HashTag saveHashTag(Long boardId, HashTag hashTag) {


        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("not found : "+ boardId));
        hashTag.setBoard(board);
        return hashTagRepository.save(hashTag);
    }

    //해시태그 목록 조회
    public List<String> findAll(long boardId) {
        return hashTagRepository.findByBoardId(boardId);
    }

    //해시태그 상세 조회
    public List<String> findById(Long boardId) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new IllegalArgumentException("not found: " + boardId));

        return hashTagRepository.findByBoardId(boardId);
    }

//    @Transactional
//    public HashTag updateHash(Long boardId, HashTag boardUpdateDTO) {
//
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new IllegalArgumentException("not found:" + boardId));
//        List<String> hashTag1 = hashTagRepository.findByBoardId(board.getId());
//        HashTag hashTag3 = new HashTag();
//        hashTag3.setHashTag(boardUpdateDTO);
//
//        return hashTag2;
//    }
}

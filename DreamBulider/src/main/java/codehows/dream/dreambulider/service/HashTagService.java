package codehows.dream.dreambulider.service;

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
public class HashTagService {

    private final HashTagRepository hashTagRepository;
    private final BoardRepository boardRepository;

//    @Transactional
//    public HashTag saveHashTag(Long boardId,AddRequestHash requestHash) {
//
//
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new IllegalArgumentException("not found : "+ boardId));
//
//        HashTag hashTag = hashTagRepository.save(requestHash.toEntity(board));
//        return hashTagRepository.save(hashTag);
//    }

    //해시태그 저장
    @Transactional
    public HashTag saveHashTag(Long boardId, HashTag hashTag) {


        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("not found : "+ boardId));
        hashTag.setBoard(board);
        return hashTagRepository.save(hashTag);
    }

//    public List<HashTag> findAll(long boardId) {
//        return hashTagRepository.findAllByBoardId(boardId);
//    }

    public HashTag findById(Long boardId) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new IllegalArgumentException("not found: " + boardId));

        return hashTagRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + boardId));
    }
}

package codehows.dream.dreambulider.service;


import codehows.dream.dreambulider.dto.BoardDTO.BoardWriteRequest;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public Board save(BoardWriteRequest request) {
        return boardRepository.save(request.toEntity());
    }
}

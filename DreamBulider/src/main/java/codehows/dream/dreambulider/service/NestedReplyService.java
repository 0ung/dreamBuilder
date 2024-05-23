package codehows.dream.dreambulider.service;


import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedReplyRequestDTO;
import codehows.dream.dreambulider.entity.NestedReply;
import codehows.dream.dreambulider.repository.NestedReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NestedReplyService {

    private final NestedReplyRepository nestedReplyRepository;

    public NestedReply saveNestedReply(NestedReplyRequestDTO nestedReplyRequestDTO) {
        return nestedReplyRepository
                        .save(NestedReply.builder()
                        .comment(nestedReplyRequestDTO.getComment())
                        .build());

    }
}

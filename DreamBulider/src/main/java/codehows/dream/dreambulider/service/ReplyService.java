package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.dto.ReplyDTO.ReplyRequestDTO;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyUpdateDTO;
import codehows.dream.dreambulider.entity.Reply;
import codehows.dream.dreambulider.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    public Reply saveReply(ReplyRequestDTO replyRequestDTO){
        if(replyRequestDTO.getComment() == null){
            throw new IllegalArgumentException("데이터 없음");
        }
        return replyRepository
                .save(Reply.builder()
                .comment(replyRequestDTO.getComment())
                .invisible(replyRequestDTO.isInvisible())
                .build());
    }

    public List<Reply> findAll() {
        return replyRepository.findAll();
    }

    public Reply findById(long id) {
        return replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    /*
    @Transactional
    public Reply delete (long id, ReplyDeleteDTO replyDeleteDTO) {
        Reply reply = replyRepository.findById(id)
                        .orElseThrow(() ->new IllegalArgumentException("삭제 실패 : " + id));
        reply.update(replyDeleteDTO.getComment());

        return reply;
    }
     */

    @Transactional
    public Reply deleteInvisible (long id) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        reply.delete();

        return reply;
    }

    @Transactional
    public Reply update(long id, ReplyUpdateDTO replyUpdateDTO) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        reply.update(replyUpdateDTO.getComment());

        return reply;
    }
}
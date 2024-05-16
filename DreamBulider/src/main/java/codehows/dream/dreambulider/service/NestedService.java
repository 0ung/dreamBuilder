package codehows.dream.dreambulider.service;


import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedRequestDTO;
import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedUpdateDTO;
import codehows.dream.dreambulider.entity.NestedReply;
import codehows.dream.dreambulider.entity.Reply;
import codehows.dream.dreambulider.repository.NestedRepository;
import codehows.dream.dreambulider.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NestedService {

    private final NestedRepository nestedRepository;
    private final ReplyRepository replyRepository;

    public NestedReply saveNestedReply(Long id, NestedRequestDTO nestedRequestDTO) {

        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("실패 : " + id));

        nestedRequestDTO.setReply(reply);

        return nestedRepository.save(nestedRequestDTO.toEntity());
    }

    @Transactional
    public List<NestedReply> findAll()  {

        return nestedRepository.findAll();
    }
    /*public List<NestedReply> findAll(Long id) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다 : id " + id));
        Map<Long, List<NestedReply>> responseData = new HashMap<>();
        List<NestedReply> nestedReplies = reply.getNestedReplies();

        return nestedReplies;
    }*/

    public NestedReply findById(long id) {
        return nestedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID못찾음 :" + id));
    }

    /*
    @Transactional
    public void delete (Long replyId, Long id) {
        NestedReply nestedReply = nestedRepository.findByReplyIdAndId(replyId, id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. : id " + id));

        nestedRepository.delete(nestedReply);
    }*/

    @Transactional
    public NestedReply deleteInvisible(long id) {
        NestedReply nestedReply = nestedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " +id ));
        nestedReply.delete();

        return nestedReply;
    }
    @Transactional
    public void update(Long replyId, Long id, NestedUpdateDTO request) {
        NestedReply nestedReply = nestedRepository.findByReplyIdAndId(replyId, id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글 수정이 안됨." + id));

        nestedReply.update(request.getComment());

    }

}

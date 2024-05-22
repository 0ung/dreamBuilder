package codehows.dream.dreambulider.service;


import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedRequestDTO;
import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedResponseDTO;
import codehows.dream.dreambulider.dto.NestedReplyDTO.NestedUpdateDTO;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.entity.NestedReply;
import codehows.dream.dreambulider.entity.Reply;
import codehows.dream.dreambulider.repository.MemberRepository;
import codehows.dream.dreambulider.repository.NestedRepository;
import codehows.dream.dreambulider.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NestedService {

    private final NestedRepository nestedRepository;
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    public NestedReply saveNestedReply(Long id, NestedRequestDTO nestedRequestDTO, String email) {

        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));

        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("실패 : " + id));

        NestedReply result = NestedReply.builder()
                .comment(nestedRequestDTO.getComment())
                .invisible(nestedRequestDTO.isInvisible())
                .member(member)
                .reply(reply)
                .build();
        nestedRepository.save(result);

        return result;
    }

    @Transactional
    public List<NestedResponseDTO> findAllByReplyId(Long replyId) {

        List<NestedReply> nestedReplies = nestedRepository.findByReplyId(replyId);
        return nestedReplies.stream()
                .map(NestedResponseDTO::new)
                .collect(Collectors.toList());
    }

    public NestedReply findById(long id) {
        return nestedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID못찾음 :" + id));
    }


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

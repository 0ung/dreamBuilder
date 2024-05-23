package codehows.dream.dreambulider.Controller;

import codehows.dream.dreambulider.dto.Member.MemberListResponseDTO;
import codehows.dream.dreambulider.entity.Board;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.repository.MemberRepository;
import codehows.dream.dreambulider.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ManageController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/memberList/{page}")
    public ResponseEntity<?> findAllMemberList(@PathVariable Optional<Integer> page) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Member> memberList;

        memberList = memberService.findAll(pageable);

        List<MemberListResponseDTO> list = new ArrayList<>();
        for(Member member : memberList) {
            MemberListResponseDTO memberListResponseDTO = new MemberListResponseDTO();
            memberListResponseDTO.setId(member.getId());
            memberListResponseDTO.setName(member.getName());
            memberListResponseDTO.setEmail(member.getEmail());
            memberListResponseDTO.setRegTime(member.getRegTime());
            memberListResponseDTO.setUpdateTime(member.getUpdateTime());
            memberListResponseDTO.setAuthority(member.getAuthority());
            memberListResponseDTO.setWithdrawal(member.isWithdrawal());
            list.add(memberListResponseDTO);
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("/memberlist/withdrawl")
    public ResponseEntity<?> withdrawal(@RequestBody Map<String, String> requestBody){
        try{
            String email = requestBody.get("email");
            memberService.withdrawal(email);
            Optional<Member> updatedMember = memberRepository.findMemberByEmail(email);
            return ResponseEntity.ok(updatedMember);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("잘못된 요청(멤버매니지)");
        }
    }

    @PostMapping("/memberlist/restore")
    public ResponseEntity<?> restore(@RequestBody Map<String, String> requestBody){
        try{
            String email = requestBody.get("email");
            memberService.restore(email);
            Optional<Member> updatedMember = memberRepository.findMemberByEmail(email);
            return ResponseEntity.ok(updatedMember);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("잘못된 요청(멤버매니지)");
        }
    }
}

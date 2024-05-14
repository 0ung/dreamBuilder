package codehows.dream.dreambulider.controller;

import codehows.dream.dreambulider.dto.MemberFormDTO;
import codehows.dream.dreambulider.dto.MemberLoginDTO;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.entity.RefreshToken;
import codehows.dream.dreambulider.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.repository.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberFormDTO memberFormDTO) {
        try{
            memberService.save(memberFormDTO);
        }catch (Exception e){
            return new ResponseEntity<>("입력이 잘못되었습니다.", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>("회원가입 완료", HttpStatus.CREATED);
    }

    @PostMapping("/useremail/exist")
    public ResponseEntity<?> exist(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        if(memberService.duplicateMemberEmail(email)){
            return new ResponseEntity<>("중복된 이메일입니다.", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("사용가능한 이메일입니다.", HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginDTO memberLoginDTO) {
        try{
            return new ResponseEntity<>(memberService.login(memberLoginDTO), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("입력정보를 확인해주세요", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam("refreshToken") String refreshToken) {
        try {
            return new ResponseEntity<>(memberService.tokenRefresh(refreshToken), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("잘못된 요청", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshToken refreshToken){
        try{
            memberService.logout(refreshToken);
            return ResponseEntity.ok("로그아웃 되었습니다.");
        } catch(Exception e){
            return ResponseEntity.badRequest().body("잘못된 요청");
        }
    }


}

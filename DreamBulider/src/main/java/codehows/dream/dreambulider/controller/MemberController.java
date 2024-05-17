package codehows.dream.dreambulider.controller;

import codehows.dream.dreambulider.dto.*;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.entity.RefreshToken;
import codehows.dream.dreambulider.jwt.TokenProvider;
import codehows.dream.dreambulider.repository.MemberRepository;
import codehows.dream.dreambulider.repository.RefreshTokenRepository;
import codehows.dream.dreambulider.service.MemberService;
import codehows.dream.dreambulider.service.RefreshTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;

    @Value("${cos.key}")
    private String cosKey;

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
    public ResponseEntity<?> login(@RequestBody MemberLoginDTO memberLoginDTO, HttpServletResponse response) {
        try{
            return new ResponseEntity<>(memberService.login(memberLoginDTO, response), HttpStatus.OK);
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

    @GetMapping("/auth/callback")
    public String authCallback(String code) {
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "95a542009e6abdf2635c87b2de0b4c5f");
        params.add("redirect_uri", "http://localhost:8080/member/auth/callback");
        params.add("code", code);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> tokenrequest = new HttpEntity<>(params, headers);

        //Http 요청하기 - POST 방식으로 - 그리고 response변수의 응답 받음
        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, tokenrequest, String.class);

        ObjectMapper obMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try {
            oauthToken = obMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("카카오 엑세스 토큰: "+oauthToken.getAccess_token());

        RestTemplate rt2 = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headers2.add("content-type", "application/x-www-form-urlencoded;charset=utf-8");


        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> profilerequest = new HttpEntity<>(headers2);

        //Http 요청하기 - POST 방식으로 - 그리고 response변수의 응답 받음
        ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, profilerequest, String.class);

        ObjectMapper obMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = obMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("카카오 아이디(번호): "+kakaoProfile.getId());
        System.out.println("카카오 프로필: "+kakaoProfile.getProperties().getNickname());
        System.out.println("카카오 이메일: "+kakaoProfile.getKakao_account().getEmail());

        MemberFormDTO kakaoUser = MemberFormDTO.builder()
                        .name(kakaoProfile.getProperties().getNickname())
                                .password(cosKey)
                                        .email(kakaoProfile.getKakao_account().getEmail())
                                                .build();

        //회원가입 처리
        if(!memberService.duplicateMemberEmail(kakaoUser.getEmail())){
            memberService.save(kakaoUser);
        }

        //로그인 처리
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(kakaoUser.getEmail(), kakaoUser.getPassword()); //getPassword를 coskey로 변경

        Authentication authentication =authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Member member = memberRepository.findMemberByEmail(authentication.getName()).orElseThrow();

        String newRefreshToken =tokenProvider.createRefreshToken();
        String accessToken = tokenProvider.createToken(member);

        RefreshToken refreshToken = refreshTokenRepository.findByMember(member).orElse(null);

        if(refreshToken == null) {
            refreshTokenService.saveRefreshToken(new RefreshToken(member, newRefreshToken));
        }else{
            refreshToken.update(newRefreshToken);
        }

        return "로그인 완료";
    }


}
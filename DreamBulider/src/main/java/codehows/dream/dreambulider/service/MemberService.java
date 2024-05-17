package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.constats.Authority;
import codehows.dream.dreambulider.dto.MemberFormDTO;
import codehows.dream.dreambulider.dto.MemberLoginDTO;
import codehows.dream.dreambulider.dto.TokenResponse;
import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.entity.RefreshToken;
import codehows.dream.dreambulider.jwt.TokenProvider;
import codehows.dream.dreambulider.repository.MemberRepository;
import codehows.dream.dreambulider.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.catalina.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    public Member save(MemberFormDTO member) {
        if(member == null) {
            throw new IllegalArgumentException("회원정보를 입력해주세요");
        }
        return memberRepository.save(Member.builder()
                .name(member.getName())
                .password(passwordEncoder.encode(member.getPassword()))
                .email(member.getEmail())
                .authority(Authority.ROLE_USER)
                .build());
    }

    public boolean duplicateMemberEmail(String email) {
        return memberRepository.existsMemberByEmail(email);
    }

    public TokenResponse login (MemberLoginDTO memberLoginDTO, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginDTO.getEmail(), memberLoginDTO.getPassword());

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

        // 액세스 토큰을 쿠키에 설정
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true); // 자바스크립트에서 접근하지 못하게 설정
        accessTokenCookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능하도록 설정
        accessTokenCookie.setMaxAge(60 * 60); // 1시간 동안 유효

        // 리프레시 토큰을 쿠키에 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 동안 유효

        // 쿠키를 응답에 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return new TokenResponse(accessToken, newRefreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findMemberByEmail(email).orElseThrow();
    }

    public TokenResponse tokenRefresh(String refreshToken) {
        if(!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("잘못된 토큰");
        }
        RefreshToken existRefreshToken = refreshTokenService.findByRefreshToken(refreshToken);

        Member member = existRefreshToken.getMember();

        String accessToken = tokenProvider.createToken(member);
        String newRefreshToken = existRefreshToken.update(tokenProvider.createRefreshToken()).getRefreshToken();

        return new TokenResponse(accessToken, newRefreshToken);
    }

    public void logout(RefreshToken refreshToken) {
        Member member = refreshTokenService.findByRefreshToken(refreshToken.getRefreshToken()).getMember();
        refreshTokenService.removeRefreshToken(member);
    }



}
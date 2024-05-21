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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    Map map = new HashMap();

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

    public Map<String,String> login (MemberLoginDTO memberLoginDTO, HttpServletResponse response) {
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
            refreshTokenService.saveRefreshToken(refreshToken);
        }

        // 액세스 토큰을 쿠키에 설정
        addCookies(response,accessToken,newRefreshToken);
        map.put("Role",member.getAuthority());
        return map;
    }

    public void addCookies(HttpServletResponse response, String accessToken, String newRefreshToken) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60);
        accessTokenCookie.setSecure(true);

        Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshTokenCookie.setSecure(true);


        // 쿠키를 응답에 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findMemberByEmail(email).orElseThrow();
    }

    public TokenResponse tokenRefresh(String refreshToken) {
        if(tokenProvider.validateToken(refreshToken) != 1) {
            throw new IllegalArgumentException("잘못된 토큰");
        }
        RefreshToken existRefreshToken = refreshTokenService.findByRefreshToken(refreshToken);

        Member member = existRefreshToken.getMember();

        String accessToken = tokenProvider.createToken(member);
        String newRefreshToken = existRefreshToken.update(tokenProvider.createRefreshToken()).getRefreshToken();

        return new TokenResponse(accessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String email,HttpServletResponse response) {
        Member member = memberRepository.findMemberByEmail(email).orElse(null);
        refreshTokenService.removeRefreshToken(member);

        Cookie accessTokenCookie = new Cookie("accessToken","");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setSecure(true);

        Cookie refreshTokenCookie = new Cookie("refreshToken","");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setSecure(true);

        // 쿠키를 응답에 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

    }

    public String getRefreshToken(HttpServletRequest request) {
        // 요청에서 쿠키 배열을 가져옴
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 쿠키 이름이 'refreshToken'과 일치하는지 확인
                if ("refreshToken".equals(cookie.getName())) {
                    // refreshToken 값을 반환
                    return cookie.getValue();
                }
            }
        }
        // refreshToken 쿠키가 없으면 메시지를 반환
        return "Refresh Token not found";
    }





}

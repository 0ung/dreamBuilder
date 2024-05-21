package codehows.dream.dreambulider.jwt;

import codehows.dream.dreambulider.entity.Member;
import codehows.dream.dreambulider.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authorizationHeader = request.getHeader("Authorization");
//        String token = getToken(authorizationHeader);
        Cookie[] cookies = request.getCookies();
// accessToken
        String token = getToken(cookies);

        if(tokenProvider.validateToken(token) == 1) {
            Authentication auth = tokenProvider.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }else if(tokenProvider.validateToken(token) == 2) {
            String refreshToken = getRefreshToken(cookies);
            if(tokenProvider.validateToken(refreshToken) == 1) {
                Authentication auth = tokenProvider.getAuthentication(refreshToken);
                Member member = memberRepository.findMemberByEmail( auth.getName()).orElseThrow();
                String accessToken = tokenProvider.createToken(member);
                addCookies(response, accessToken);

                Authentication auth2 = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth2);
            }else if(tokenProvider.validateToken(refreshToken) == 2) {
                try{
                    response.sendRedirect("http://localhost:5173/login");
                }catch (Exception e){}
            }
        }

        filterChain.doFilter(request, response);
    }


    private String getToken(Cookie[] cookies) {
        if(cookies != null && cookies.length > 0) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("accessToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String getRefreshToken(Cookie[] cookies) {
        if(cookies != null && cookies.length > 0) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("RefreshToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void addCookies(HttpServletResponse response, String accessToken) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60);
        accessTokenCookie.setSecure(true);

        // 쿠키를 응답에 추가
        response.addCookie(accessTokenCookie);
    }
}

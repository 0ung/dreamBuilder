package codehows.dream.dreambulider.jwt;

import codehows.dream.dreambulider.entity.Member;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@RequiredArgsConstructor
@Service
@Log4j2
public class TokenProvider {
    private static final String TOKEN_PREFIX = "auth";
    private final JwtProperties jwtProperties;

    public String createToken(Member member) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 7 * 60 * 60 * 24);
        String autorities = member.getAuthority().toString();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(member.getEmail())
                .claim(TOKEN_PREFIX, autorities)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, 30);

        Date expiry = calendar.getTime();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public int validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return 1;
        } catch (ExpiredJwtException e) {
            return 2;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (MalformedJwtException e) {
            log.info("손상된 JWT 토큰입니다.");
        } catch (SignatureException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (IllegalArgumentException e) {
            log.info("잘못된 JWT 토큰입니다.");
        } catch (Exception e) {
            log.info("유효하지 않는 JWT 토큰입니다.");
        }
        return 0;
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        if(claims.get(TOKEN_PREFIX) == null){
            throw new RuntimeException("권한이 없습니다.");
        }

        String email = claims.getSubject();
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(TOKEN_PREFIX).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();
    return new UsernamePasswordAuthenticationToken(new User(email, "", authorities), token, authorities);
    }
}

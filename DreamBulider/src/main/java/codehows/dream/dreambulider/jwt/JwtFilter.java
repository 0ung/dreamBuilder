package codehows.dream.dreambulider.jwt;

import codehows.dream.dreambulider.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = request.getHeader("Authorization");
		String token = getAccessToken(authorizationHeader);

		if (token != null && tokenProvider.validateToken(token) == 1) {
			Authentication auth = tokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
		} else if (token != null && tokenProvider.validateToken(token) == 2) {
			Cookie[] cookies = request.getCookies();
			String refreshToken = getRefreshToken(cookies);
			if (refreshToken != null && tokenProvider.validateToken(refreshToken) == 1) {
				Authentication auth = tokenProvider.getAuthentication(refreshToken);
				Member member = tokenProvider.findMember(auth.getName());
				String newAccessToken = tokenProvider.createToken(member);
				addCookies(response, newAccessToken);
				Authentication newAuth = tokenProvider.getAuthentication(newAccessToken);
				SecurityContextHolder.getContext().setAuthentication(newAuth);
			} else if (refreshToken == null || tokenProvider.validateToken(refreshToken) == 2) {
				response.sendRedirect("http://localhost:5173/login");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private String getAccessToken(String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7);
		}
		return null;
	}

	private String getRefreshToken(Cookie[] cookies) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("refreshToken".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	private void addCookies(HttpServletResponse response, String accessToken) {
		Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
		accessTokenCookie.setHttpOnly(true);
		accessTokenCookie.setPath("/");
		response.addCookie(accessTokenCookie);
	}

}

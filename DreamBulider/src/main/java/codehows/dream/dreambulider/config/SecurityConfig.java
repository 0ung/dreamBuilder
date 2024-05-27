package codehows.dream.dreambulider.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import codehows.dream.dreambulider.jwt.JwtFilter;
import codehows.dream.dreambulider.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final TokenProvider tokenProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(
				logout -> logout.clearAuthentication(true)
			)
			.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			).addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(
				request -> {
					request.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
						.requestMatchers(antMatcher("/member/**")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/api/boards/**")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/api/liked")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/api/board/**")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/api/reply")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/api/rereply/**")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/files/**")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/temp/**")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/download/files/**")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/api/main")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/api/read/**")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/api/admin/reply/title/**")).permitAll()
						.requestMatchers(antMatcher(HttpMethod.GET,"/api/read/total/**")).permitAll()
						.anyRequest().authenticated();
				}
			)
			.cors(
				cors -> cors.configurationSource(corsConfigurationSource())
			);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedOrigin("http://222.119.100.90:8219");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setMaxAge(86400L);
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}

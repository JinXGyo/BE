package LDHD.project.common.config;

import LDHD.project.common.security.jwt.JwtAuthenticationFilter;
import LDHD.project.common.security.jwt.JwtTokenProvider;
import LDHD.project.common.security.oauth.OAuth2SuccessHandler;
import LDHD.project.domain.user.Role;
import LDHD.project.domain.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableJpaAuditing // spring security 기능 활성화
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. csrf 보안 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 2. Http Basic 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                // 3. Form 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // 4. 세션 관리 정책 설정 -> STATLESS: 우리 프로젝트에선 세션 방식 X
                .sessionManagement(management
                        -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //5. URL 별 권한 관리
                .authorizeHttpRequests(auth -> auth
                        // 정적 자원(css, js, image), 메인 페이지, 로그인 관련 URL 모두 허용
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console","/login/**"). permitAll()
                        // "/api/**"로 시작하는 요청은 인증된 유저만 접근 가능
                        .requestMatchers("/api/**").hasRole(Role.USER.name())
                        // 그 외 나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        // 로그인 성공 시 -> JWT 발급
                        .successHandler(oAuth2SuccessHandler)
                        // 사용자 정보 가져오기(DB 저장)
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                )
                // JWT 인증 필터 적용(기본 로그인 필터 앞에서 먼저 JWT 토큰 인증 할 수 있도록)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

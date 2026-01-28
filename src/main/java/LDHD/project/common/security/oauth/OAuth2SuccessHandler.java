package LDHD.project.common.security.oauth;

import LDHD.project.common.security.jwt.JwtTokenProvider;
import LDHD.project.domain.user.repository.UserRepository;
import LDHD.project.domain.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException{
        // 구글 로그인 성공 후 사용자 정보(principal) 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email"); // 구글이 넘겨준 이메일

        // DB에서 유저 조회(Role 권한 정보 가져오기 위함)
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));

        // JWT 토큰 생성(JwtTokenProvider 사용)
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoleKey());
        log.info("Google Login Success! JWT Token: {}", token);

        // 리다이렉트 URI 설정 (로그인 성공 시 토큰을 쿼리 파라미터에 담아 전달)
        //"/login-success" 또는 메인 페이지로 설정해야함! 현재 test 시 화면 이동이 되지 않지만 DB에는 저장됨
        String targetUrl = UriComponentsBuilder.fromUriString("/login-success")
                .queryParam("token", token)
                .build()
                .toUriString();

        // 리다이렉트 수행
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

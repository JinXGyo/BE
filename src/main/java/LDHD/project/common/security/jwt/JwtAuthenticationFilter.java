package LDHD.project.common.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        // Request 헤더 부에서 JWT token 추출
        String token = resolveToken((HttpServletRequest) request);

        // validateToken으로 토큰 유효성 검사
        if(token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하다면 정보(이메일) 받아옴
            String email = jwtTokenProvider.getUserEmail(token);

            // 인증 객체 생성 -> DB 조회 거치지 않고 토큰의 정보만으로 유저 객체 만듦.
            UserDetails principal = User.builder()
                    .username(email)
                    .password("")
                    .authorities("ROLE_USER")
                    .build();

            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());

            // SecurityContext에 Authentication 객체 저장 => 이 로직 이후로 인증된 사용자로 간주
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 다음 필터로 요청 넘김
        chain.doFilter(request, response);

    }

    // Request 헤더에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {

        // "Authorization: Bearer abcd.efgh.ijkl" 형태에서 "Bearer(권한을 달라는 방식)"를 떼어내는 작업
        // => 불필요한 Bearer 제거, 순수 토큰인 abcd.efgh.ijkl 값만 추출하도록
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

            // 공백포함 7글자(Bearer) 제거
            return bearerToken.substring(7);
        }
        return null;
    }
}

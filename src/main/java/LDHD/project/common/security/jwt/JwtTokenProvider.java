package LDHD.project.common.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;

@Slf4j // 로그 출력
@Component // 빈으로 등록
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    private final long tokenValidTime = 30 * 60 * 1000L;

    @PostConstruct
    protected void init() {
        // yml에 설정한 secretkey 를 디코딩 -> 바이트 배열로 만듦
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // 바이트 배열 사용해 암호화 키 객체 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성 : 로그인 성공 시 이메일과 권한 받아 JWT 만듦
    public String createToken(String email, String role){
        // Claims: 토큰 내부 정보를 담을 객체 / 토큰 주체를 이메일로 설정 -> 나중에 이메일로 권한 확인 위함
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) //발행 시간
                .setExpiration(new Date(now.getTime()+ tokenValidTime)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 암호화 키와 알고리즘
                .compact(); // 위 설정들로 토큰 문자열 생성 및 반환
    }
    // 토큰에서 회원 정보(이메일) 추출
    public String getUserEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key) // 비밀 키 넣어 서명 맞는지 확인
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 + 만료일자 확인
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key) // 비밀키로 검증
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다.");
        }catch (ExpiredJwtException e){
            log.info("만료된 JWT 토큰입니다.");
        }catch (UnsupportedJwtException e){
            log.info("지원되지 않는 JWT 토큰입니다.");
        }catch (IllegalArgumentException e){
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}

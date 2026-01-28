package LDHD.project.common.security.oauth;

import LDHD.project.domain.user.Role;
import LDHD.project.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    // 구글에서 전송한 JSON 데이터(이름, 이메일 등)을 User 엔티티로 변환
    private Map<String, Object> attributes; //OAuth2 반환하는 유저 정보(원본)
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    // OAuth2User에서 반환하는 정보는 Map이므로 값 하나씩 변환
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        // registrationId: 나중에 구글, 네이버, 카카오 중 어떤 로그인인지 식별 -> 지금은 크게 필요 x
        return ofGoogle(userNameAttributeName, attributes);
    }

    // 구글 생성자
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes
                .builder()
                .name((String) attributes.get("name")) //Map에서 "name"꺼내 String으로 변환
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // DB에 저장할 User 객체 생성 => 신규가입 시
    // 기존 회원의 로그인이면 toEntity 호출이 아닌 기존 User 엔티티 사용
    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();
    }
}

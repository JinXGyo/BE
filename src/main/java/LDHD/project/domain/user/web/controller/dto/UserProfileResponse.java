package LDHD.project.domain.user.web.controller.dto;

import LDHD.project.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

    private Long user_id;
    private String loginId;
    private String name;
    private String email;
    private LocalDateTime created_at;

    private ProfileDetail profile; // 중복되는 프로필 객체

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileDetail {
        private Integer gender;
        private String birthDate; // JSON 출력: "ex) 2002-05-20"
    }

    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
                .user_id(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .email(user.getEmail())
                .created_at(user.getCreatedAt())
                .profile(ProfileDetail.builder()
                        .gender(user.getGender())
                        .birthDate(user.getBirthDate() != null ? user.getBirthDate().toString() : null)
                        //LocalDate를 String으로 변환 -> yyyy-mm-dd 형식
                        .build())
                .build();
    }
}

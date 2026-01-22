package LDHD.project.domain.user.web.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Getter
public class CreateUserResponse {

    String name;

    public CreateUserResponse(String name) {

        this.name = name;
    }
}

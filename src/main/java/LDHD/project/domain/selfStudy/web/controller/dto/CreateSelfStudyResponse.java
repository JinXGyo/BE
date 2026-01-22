package LDHD.project.domain.selfStudy.web.controller.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateSelfStudyResponse {

    Long id;
    String title;
    String description;

    public CreateSelfStudyResponse(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}

package LDHD.project.domain.selfStudy.web.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateSelfStudyResponse {

    Long selfStudyId;
    String title;
    String description;

    public UpdateSelfStudyResponse(Long selfStudyId, String title, String description) {
        this.selfStudyId = selfStudyId;
        this.title = title;
        this.description = description;
    }
}

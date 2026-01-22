package LDHD.project.domain.bookmark.web.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateBookmarkResponse {

    Long id;
    Long selfStudyId;
    Long userId;

    public CreateBookmarkResponse(Long id, Long selfStudyId, Long userId) {

        this.id = id;
        this.selfStudyId = selfStudyId;
        this.userId = userId;
    }
}

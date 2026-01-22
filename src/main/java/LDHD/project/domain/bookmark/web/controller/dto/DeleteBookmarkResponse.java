package LDHD.project.domain.bookmark.web.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteBookmarkResponse {

    Long userId;
    Long selfStudyId;

    public DeleteBookmarkResponse( Long userId, Long selfStudyId) {
        this.userId = userId;
        this.selfStudyId = selfStudyId;
    }

}

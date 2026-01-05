package LDHD.project.domain.selfStudy.web.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateSelfStudyRequest {

    String title;
    String description;
}

package LDHD.project.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    OK(HttpStatus.OK, "COMMON200", "SUCCESS!"),
    CREATED(HttpStatus.CREATED, "COMMON201", "CREATED!"),
    DELETED(HttpStatus.OK, "COMMON202", "DELETED!"),
    UPDATED(HttpStatus.OK, "COMMON203", "UPDATED!");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
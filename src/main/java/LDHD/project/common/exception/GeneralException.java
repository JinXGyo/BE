package LDHD.project.common.exception;

import LDHD.project.common.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private ErrorCode errorCode;
}

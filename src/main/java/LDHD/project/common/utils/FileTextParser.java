package LDHD.project.common.utils;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class FileTextParser {
    // AI 학습용 ->업로드 된 파일 형식(pdf,docx..)과 무관하게 text만 뽑기 위함
    // 필요 없으면 삭제 가능

    // Tika : 파일 형식 자동 감지 후 text만 추출
    // 생성 비용이 있으므로 하나만 만들어 재사용
    private final Tika tika = new Tika();

    public String extractText(MultipartFile file) {
        try {
            // PDF, Word, PPT, TXT 등의 파일 내용을 자동으로 감지하여 텍스트만 추출
            return tika.parseToString(file.getInputStream());
        } catch (IOException | org.apache.tika.exception.TikaException e) {
            // 텍스트 추출 실패 시 실패 로그 남기고 빈 문자열 반환
            // -> 에러 반환 x (업로드는 그대로 되도록)
            return "";
        }
    }
}
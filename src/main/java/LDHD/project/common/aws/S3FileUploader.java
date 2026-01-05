package LDHD.project.common.aws;

import LDHD.project.common.exception.GeneralException;
import LDHD.project.common.response.ErrorCode;
import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3FileUploader {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file){
        if(file.isEmpty()){
            throw new GeneralException(ErrorCode.FILE_EMPTY);
        }

        // 1. 중복 방지를 위한 랜덤 UUID 파일명 생성
        String originalFileName = file.getOriginalFilename();
        String uuidFileName = UUID.randomUUID() + "_" + originalFileName;

        try (InputStream inputStream = file.getInputStream()) {
            s3Template.upload(bucket, uuidFileName, inputStream);
            return s3Template.download(bucket, uuidFileName).getURL().toString();
        }
        catch (IOException e) {
            log.error("파일 업로드 IO 에러", e);
            throw new RuntimeException("파일 업로드에 실패했습니다.");
        }
        catch (S3Exception e) {
            log.error("AWS S3 에러", e);
            throw new RuntimeException("S3 서버와 통신 중 문제가 발생했습니다.");
        }
    }

    public void deleteFile(String fileUrl) {
        // 1. 전체 URL에서 파일명(Key)만 추출
        // 예: https://s3.region.amazonaws.com/bucket-name/uuid_filename.jpg
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

        try {
            // 2. 한글 파일명 깨짐 방지
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

            // 3. S3에서 삭제
            s3Template.deleteObject(bucket, decodedFileName);
            log.info("S3 파일 삭제 완료: {}", decodedFileName);

        } catch (S3Exception e) {
            log.error("S3 파일 삭제 실패", e);
            throw new RuntimeException("S3 서버에서 파일을 삭제하지 못했습니다.");
        }
    }
}


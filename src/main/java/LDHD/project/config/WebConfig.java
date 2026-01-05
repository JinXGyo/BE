package LDHD.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer { //파일과 함께 json 형식 업로드시 에러 방지 위함

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 등록된 모든 데이터 변환기(converter) 목록 돌면서 Jackson 컨버터(JSON 처리기)를 찾음
        for (HttpMessageConverter<?> converter : converters) {
            //Jackson 컨버터: JSON -> 자바 객체로 변환
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {

                // Jackson 컨버터에게 "application/octet-stream" 타입도 JSON처럼 처리 등록
                jacksonConverter.setSupportedMediaTypes(Arrays.asList(
                        MediaType.APPLICATION_JSON, //JSON 형식
                        MediaType.APPLICATION_OCTET_STREAM //파일
                ));
            }
        }
    }
}
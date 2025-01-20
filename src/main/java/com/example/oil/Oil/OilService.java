package com.example.oil.Oil;

// 2. 서비스(Service) 클래스 생성
// 외부 API를 호출하고 데이터를 DTO로 매핑하는 역할
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Configuration
public class OilService {

    @Value("${API-KEY}")
    private String API_KEY;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> fetchOilDataAsMap() {

// 서구 지역만 먼저 가져와 보자

        String apiUrl = API_KEY; // 실제 API URL
        try {
            // JSON 응답을 String으로 받음
            String response = restTemplate.getForObject(apiUrl, String.class);
            System.out.println("API Response: " + response);

            // JSON 문자열을 Map으로 변환
            Map<String, Object> responseData = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            System.out.println("Mapped Map: " + responseData);

            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OilService {

    @Value("${API-KEY}")
    private String API_KEY;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Map<String, Object>> fetchOilDataAsMap(List<String> file) {
        List<Map<String, Object>> gasStationDetails = new ArrayList<>();
        // 서구 지역만 먼저 가져와 보자
        for (String gasStationId : file){
            try {
                System.out.println(gasStationId);
                String apiUrl = "https://www.opinet.co.kr/api/detailById.do?code=" + API_KEY + "&id=" + gasStationId +"&out=json";
                //System.out.println(apiUrl);
                String response = restTemplate.getForObject(apiUrl, String.class);

                //System.out.println("API Response: " + response);

                // JSON 문자열을 Map으로 변환
                Map<String, Object> responseData = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
                //System.out.println("Mapped Map: " + responseData);

                gasStationDetails.add(responseData);
                //System.out.println("테스트 : " + gasStationDetails);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        //System.out.println("test" + gasStationDetails);

        return gasStationDetails;
    }

    public List<String> readFile(String filePath){
        List<String> gasList = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = br.readLine()) != null){
                gasList.add(line.trim());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return gasList;
    }
}

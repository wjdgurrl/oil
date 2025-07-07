package com.example.oil.Oil;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;



@Service
public class OilService {

    @Value("${APIKEY}")
    private String API_KEY;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public String filePath(String goo){
        String file=" ";
        switch (goo){
            case "seogu" :
                file = "seogu.csv";
                break;
            case "bukgoo" :
                file = "bukgoo.csv";
                break;
            case "namgoo" :
                file = "namgoo.csv";
                break;
            case "gwangsangoo" :
                file = "gwangsangoo.csv";
                break;
            case "onnuri" :
                file = "onnuri.csv";
                break;
            default:
                System.out.println("file not found");
                return null;
        }
        return file;
    }

    public List<OilDataDto> fetchOilDataAsMap(List<String> file) {
        List<OilDataDto> gasStationDetails = new ArrayList<>();

        for (String gasStationId : file){
            try {
                String apiUrl = "https://www.opinet.co.kr/api/detailById.do?code=" + API_KEY + "&id=" + gasStationId + "&out=json";
                String response = restTemplate.getForObject(apiUrl, String.class);

                // json파싱
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode resultNode = rootNode.path("RESULT"); // RESULT 노드 가져오기
                if (resultNode.isNull() || !resultNode.has("OIL")) { // RESULT나 OIL이 null인 경우 처리
                    System.err.println("RESULT or OIL is null for station ID: " + gasStationId);
                    continue; // 다음 주유소 ID로 넘어감
                }

                // dto 매핑
                OilDataDto oilDataDto = new OilDataDto();
                OilDataDto.Result result  = new OilDataDto.Result();

                JsonNode oilListNode = resultNode.path("OIL");

                // 주유소 데이터를 리스트로 변환
                if (oilListNode.isArray()) {
                    List<OilDataDto.OilStation> oilStations = new ArrayList<>();
                    for (JsonNode oilNode : oilListNode) {
                        OilDataDto.OilStation oilStation = objectMapper.treeToValue(oilNode, OilDataDto.OilStation.class);

                        // 유종 데이터를 설정
                        List<OilDataDto.OilPrice> oilPrices = parseOilPrices(oilNode.path("OIL_PRICE"));
                        oilStation.setOilPrice(oilPrices);

                        oilStations.add(oilStation);
                    }
                    result.setOil(oilStations);
                } else if (oilListNode.isObject()) {
                    OilDataDto.OilStation oilStation = objectMapper.treeToValue(oilListNode, OilDataDto.OilStation.class);

                    // 유종 데이터를 설정
                    List<OilDataDto.OilPrice> oilPrices = parseOilPrices(oilListNode.path("OIL_PRICE"));
                    oilStation.setOilPrice(oilPrices);

                    result.setOil(List.of(oilStation));
                }

                oilDataDto.setResult(result);
                gasStationDetails.add(oilDataDto);
            } catch (Exception e) {
                System.err.println("Error processing gas station ID: " + gasStationId + " - " + e.getMessage());
            }
        }

        return gasStationDetails;
    }
    private List<OilDataDto.OilPrice> parseOilPrices(JsonNode oilPriceNode) throws JsonProcessingException {
        List<OilDataDto.OilPrice> oilPrices = new ArrayList<>();

        if (oilPriceNode.isArray()) {
            for (JsonNode priceNode : oilPriceNode) {
                OilDataDto.OilPrice oilPrice = objectMapper.treeToValue(priceNode, OilDataDto.OilPrice.class);

                // PRODCD 한글 변환
                oilPrice.setProdCd(mapProdCdToFuelType(oilPrice.getProdCd()));

                oilPrices.add(oilPrice);
            }
        } else if (oilPriceNode.isObject()) {
            OilDataDto.OilPrice oilPrice = objectMapper.treeToValue(oilPriceNode, OilDataDto.OilPrice.class);

            // PRODCD 한글 변환
            oilPrice.setProdCd(mapProdCdToFuelType(oilPrice.getProdCd()));

            oilPrices.add(oilPrice);
        }

        return oilPrices;
    }

    // PRODCD 값을 한글로 변환
    private String mapProdCdToFuelType(String prodCd) {
        switch (prodCd) {
            case "B027":
                return "휘발유";
            case "D047":
                return "경유";
            case "B034":
                return "고급휘발유";
            case "C004":
                return "실내등유";
            case "K015":
                return "LPG";
            default:
                return "알 수 없음";
        }
    }

    public List<String> readFile(String filePath){
        List<String> gasList = new ArrayList<>();

        try{
            ClassPathResource resource = new ClassPathResource(filePath);
            InputStream inputStream = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
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

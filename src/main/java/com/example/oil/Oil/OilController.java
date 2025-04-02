package com.example.oil.Oil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
public class OilController {

    @Value("${API-KEY}")
    private String API_KEY;

    @Value("${naverApiKey}")
    private String NAVER_API_KEY;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OilService oilService;

    @GetMapping("/")
    public String defaultOil() {
        return "redirect:/oilPrice";
    }

    @GetMapping("/credit")
    public String credit() {
        return "credit";
    }

    @GetMapping("/testOil")
    public String testOil(Model model) {
        /*model.addAttribute("address", "광주 서구 풍서좌로 83 (매월동)");
        model.addAttribute("naverApiKey", NAVER_API_KEY);*/

        return "testOil";
    }

    @GetMapping("/oilPrice")
    public String main(){
        return "main";
    }
    @GetMapping("/get")
    public String get(Model model){
        List<String> data = new ArrayList<>();
        List<String> file = oilService.readFile("src/main/resources/gwangsangoo.csv");
        for (String s : file) {
            try {
                String apiUrl = "https://www.opinet.co.kr/api/searchByName.do?code=" + API_KEY + "&out=json&osnm=" + s + "&area=16";
                System.out.println(apiUrl);
                String response = restTemplate.getForObject(apiUrl, String.class);
                System.out.println(response);
                data.add(response);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        model.addAttribute("data", data);
        return "get";
    }


    @GetMapping("/oilPrice/{goo}")
    public String showOilPrices(@PathVariable String goo, Model model) {

        String filePath = oilService.filePath(goo);
        List<String> file = oilService.readFile(filePath);
        //System.out.println(file);

        List<OilDataDto> oilDataList = oilService.fetchOilDataAsMap(file);
        List<Map<String, Object>> oilDataForView = new ArrayList<>();
        for (OilDataDto oilData : oilDataList) {
            if (oilData.getResult() != null && oilData.getResult().getOil() != null) {
                for (OilDataDto.OilStation oilStation : oilData.getResult().getOil()) {
                    if (oilStation != null) {
                        Map<String, Object> stationData = new HashMap<>();
                        stationData.put("OS_NM", oilStation.getOsNm());
                        stationData.put("NEW_ADR", oilStation.getNewAdr());
                        stationData.put("TEL", oilStation.getTel());
                        stationData.put("LPG_YN",oilStation.getLpgYn());

                        // 1. katec 좌표 (안됨)
                        //stationData.put("GISXCOOR", oilStation.getGisXCoor());
                        //stationData.put("GISYCOOR", oilStation.getGisYCoor());

                        // 2. 위도 경도만 보내기
                        /*coordinates = oilService.coordinateConverter(Double.parseDouble(oilStation.getGisXCoor()),Double.parseDouble(oilStation.getGisYCoor()));
                        stationData.put("GISXCOOR",coordinates[0]);
                        stationData.put("GISYCOOR",coordinates[1]);*/

                        List<Map<String, String>> priceList = new ArrayList<>();
                        if (oilStation.getOilPrice() != null) {
                            for (OilDataDto.OilPrice oilPrice : oilStation.getOilPrice()) {
                                if (oilPrice != null) {
                                    Map<String, String> priceData = new HashMap<>();
                                    priceData.put("prodCdName",oilPrice.getProdCd());
                                    priceData.put("price", oilPrice.getPrice());
                                    try {
                                        double sangsaengPrice = Double.parseDouble(oilPrice.getPrice()) * 0.93;
                                        priceData.put("sangsaengPrice", String.format("%.0f", sangsaengPrice));
                                    } catch (NumberFormatException e) {
                                        priceData.put("sangsaengPrice", "가격 오류");
                                    }
                                    priceData.put("tradeDt", oilPrice.getTradeDt());
                                    priceData.put("tradeTm", oilPrice.getTradeTm());
                                    priceList.add(priceData);
                                }
                            }
                        }
                        stationData.put("oilPrice", priceList);
                        oilDataForView.add(stationData);
                    }
                }
            }
        }
        model.addAttribute("naverApiKey", NAVER_API_KEY);
        model.addAttribute("result", oilDataForView);

        return "oilPrices";
    }
}
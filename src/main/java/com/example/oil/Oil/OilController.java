package com.example.oil.Oil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class OilController {

    @Value("${APIKEY}")
    private String API_KEY;

    @Value("${naverApiKey}")
    private String NAVER_API_KEY;

    @Value("${pass}")
    private String PASS;

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

    @GetMapping("/edit")
    public String edit(@RequestParam String password) {
        boolean isValid = password.equals(PASS);
        return isValid ? "edit" : "credit"; // 뷰 이름 반환
    }


    @GetMapping("/check")
    public String check(@RequestParam String id) throws UnsupportedEncodingException {
        String encodedId = URLEncoder.encode(id, "UTF-8");

        String apiUrl = "https://www.opinet.co.kr/api/searchByName.do?code=" + API_KEY +
                "&out=xml&osnm=" + encodedId;

        // 브라우저가 외부 링크로 완전히 이동하게 함
        return "redirect:" + apiUrl;
    }

    @GetMapping("/oilPrice")
    public String main(){
        return "main";
    }


    @GetMapping("/oilPrice/{goo}")
    public String showOilPrices(@PathVariable String goo, Model model) {
        String filePath = oilService.filePath(goo);
        List<String> file = oilService.readFile(filePath);

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

                        List<Map<String, String>> priceList = new ArrayList<>();
                        if (oilStation.getOilPrice() != null) {
                            for (OilDataDto.OilPrice oilPrice : oilStation.getOilPrice()) {
                                if (oilPrice != null) {
                                    Map<String, String> priceData = new HashMap<>();
                                    priceData.put("prodCdName",oilPrice.getProdCd());
                                    priceData.put("price", oilPrice.getPrice());
                                    try {
                                        double sangsaengPrice;
                                        sangsaengPrice = Double.parseDouble(oilPrice.getPrice()) * 0.93;
                                        if(goo.equals("onnuri")){
                                            sangsaengPrice = Double.parseDouble(oilPrice.getPrice()) * 0.9;
                                        }
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
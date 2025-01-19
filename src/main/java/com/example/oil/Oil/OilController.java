package com.example.oil.Oil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class OilController {

    @Autowired
    private OilService oilService;

    @GetMapping("/")
    public String showOilPrices(Model model) {
        Map<String, Object> oilData = oilService.fetchOilDataAsMap();

        if (oilData == null || !oilData.containsKey("RESULT")) {
            model.addAttribute("error", "No data available from API.");
            return "error"; // 오류 페이지로 이동 (필요 시 HTML 파일 생성)
        }

        Map<String, Object> result = (Map<String, Object>) oilData.get("RESULT");
        model.addAttribute("oilList", result.get("OIL")); // OIL 리스트를 모델에 추가

        return "oilPrices"; // oilPrices.html 렌더링
    }
}
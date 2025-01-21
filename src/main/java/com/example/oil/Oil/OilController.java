package com.example.oil.Oil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class OilController {

    @Autowired
    private OilService oilService;

    @GetMapping("/")
    public String showOilPrices(Model model) {

        String filePath = "src/main/resources/seogu.csv";
        List<String> file = oilService.readFile(filePath);
        System.out.println(file);

        List<Map<String, Object>> oilData = oilService.fetchOilDataAsMap(file);
        List<Map<String, Object>> allOilData = new ArrayList<>();
        //System.out.println(oilData);
        for (Map<String, Object> map : oilData) {
            try {
                if (map == null || !map.containsKey("RESULT")) {
                    continue;
                }

                Map<String, Object> result = (Map<String, Object>) map.get("RESULT");
                List<Map<String, Object>> oilList = (List<Map<String, Object>>) result.get("OIL");
                if (oilList != null) {
                    allOilData.addAll(oilList); // 리스트에 모든 데이터를 추가
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //System.out.println("테스트" + allOilData);
        model.addAttribute("result", allOilData);

        return "oilPrices"; // oilPrices.html 렌더링
    }
}
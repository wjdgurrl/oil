package com.example.oil.Oil;

import java.util.List;
import lombok.Data;

@Data
public class OilDataDto {
    private Result RESULT;

    @Data
    public static class Result {
        private List<Oil> OIL;

        @Data
        public static class Oil {
            private String TRADE_DT;
            private String PRODCD;
            private String PRODNM;
            private String PRICE;
            private String DIFF;
        }
    }
}

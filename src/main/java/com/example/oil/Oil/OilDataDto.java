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
            private String UNI_ID; //주유소 코드
            private String POLL_DIV_CD; //상표
            private String GPOLL_DIV_CD; // 충전소 상표
            private String OS_NM; // 상호
            private String VAM_ADR; // 지번 주소
            private String NEW_ARD; // 도로명 주소
            private String SIGUNCD; // 소재지 시군코드
            private String LPG_YN; // 업종구분
            private String GIS_X_COOR; // GIS X좌표
            private String GIS_Y_COOR; // GIS Y좌표
        }
    }
}

package com.example.oil.Oil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OilDataDto {

    @JsonProperty("RESULT")
    private Result result;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        @JsonProperty("OIL")
        private List<OilStation> oil;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OilStation {
        @JsonProperty("UNI_ID")
        private String uniId; // 주유소 코드
        @JsonProperty("POLL_DIV_CD")
        private String pollDivCd; // 상표
        @JsonProperty("GPOLL_DIV_CD")
        private String gpollDivCd; // 상표
        @JsonProperty("OS_NM")
        private String osNm; // 상호
        @JsonProperty("VAN_ADR")
        private String vanAdr; // 지번주소
        @JsonProperty("NEW_ADR")
        private String newAdr; // 도로명 주소
        @JsonProperty("TEL")
        private String tel; // 전화번호
        @JsonProperty("SIGUNCD")
        private String sigunCd; // 소재지역 시군코드
        @JsonProperty("LPG_YN")
        private String lpgYn; // 업종구분
        @JsonProperty("MAINT_YN")
        private String maintYn; // 경정비 시설 존재 여부
        @JsonProperty("CAR_WASH_YN")
        private String carWashYn; // 세차장 존재 여부
        @JsonProperty("KPETRO_YN")
        private String kpetroYn; // 품질인증 주유소 여부
        @JsonProperty("CVS_YN")
        private String cvsYn; // 편의점 존재 여부
        @JsonProperty("GIS_X_COOR")
        private String gisXCoor; // GIS X좌표
        @JsonProperty("GIS_Y_COOR")
        private String gisYCoor; // GIS Y좌표

        @JsonProperty("OIL_PRICE")
        private List<OilPrice> oilPrice; // 오일가격
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OilPrice {
        @JsonProperty("PRODCD")
        private String prodCd; // 유종
        @JsonProperty("PRICE")
        private String price; // 판매가격
        @JsonProperty("TRADE_DT")
        private String tradeDt; // 기준일자
        @JsonProperty("TRADE_TM")
        private String tradeTm; // 기준시각
    }
}

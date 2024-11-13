package com.bigs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForecastItem {
    private String baseDate;   // "20241113"
    private String baseTime;   // "0200"
    private String category;   // "TMP", "UUU", ...
    private String fcstDate;   // "20241113"
    private String fcstTime;   // "0300"
    private String fcstValue;  // "6", "-1.3", "강수없음" 등
    private int nx;            // 61
    private int ny;            // 131
}

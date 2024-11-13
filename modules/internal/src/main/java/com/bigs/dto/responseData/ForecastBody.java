package com.bigs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForecastBody {
    private String dataType;   // "JSON"
    private ForecastItems items; // items 객체
}

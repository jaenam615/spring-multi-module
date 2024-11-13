package com.bigs.dto;

import com.bigs.dto.responseData.ForecastHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForecastResponseDto {
    private ForecastHeader header; // header 객체
    private com.bigs.dto.ForecastBody body;     // body 객체
}

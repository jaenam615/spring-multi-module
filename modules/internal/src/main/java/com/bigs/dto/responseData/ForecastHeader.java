package com.bigs.dto.responseData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForecastHeader {
    private String resultCode;  // "00"
    private String resultMsg;   // "NORMAL_SERVICE"
}

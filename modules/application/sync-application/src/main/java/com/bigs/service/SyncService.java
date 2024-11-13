package com.bigs.service;

import com.bigs.dto.ForecastDto;
import com.bigs.dto.ForecastResponseDto;
import com.bigs.entity.Forecast;
import com.bigs.repository.ForecastRepository;
import org.apache.tomcat.util.json.JSONFilter;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class SyncService {

    private final ForecastRepository forecastRepository;
//    private final RestTemplate restTemplate;

//    public SyncService(ForecastRepository forecastRepository, RestTemplate restTemplate){
    public SyncService(ForecastRepository forecastRepository){
        this.forecastRepository = forecastRepository;
//        this.restTemplate = restTemplate;
    }

    @Transactional
    public void fetchAndSaveForecastData(){
        String url = makeRequestUrl();
        System.out.println(url);

        RestTemplate restTemplate = new RestTemplate();

        try {
            System.out.println("hello world!");

            System.out.println(restTemplate.getForObject(url, String.class));


            // ForecastResponseDto로 응답 받기
//            ForecastResponseDto response = restTemplate.getForObject(url, ForecastResponseDto.class);

//            if (response != null && response.getResponse() != null && response.getResponse().getBody() != null) {
//                // items에서 item 리스트 가져오기
//                List<ForecastDto> forecastDtos = response.getResponse().getBody().getItems().getItem();
//
//                for (ForecastDto dto : forecastDtos) {
//                    Forecast forecast = new Forecast();
//                    forecast.setBaseDate(dto.getBaseDate());
//                    forecast.setBaseTime(dto.getBaseTime());
//                    forecast.setCategory(dto.getCategory());
//                    forecast.setFcstDate(dto.getFcstDate());
//                    forecast.setFcstTime(dto.getFcstTime());
//                    forecast.setFcstValue(dto.getFcstValue());
//                    forecast.setNx(dto.getNx());
//                    forecast.setNy(dto.getNy());
//                    forecastRepository.save(forecast);
//                }
//            }
        } catch (Exception e) {
            throw new RuntimeException("단기예보 조회에 실패했습니다:" + e.getMessage());
        }
    }


    /**
     * 기준 시간에 대해 요청 URL을 만드는 메서드
     * @return url 기상청 오픈 API로 요청 보낼 URL
     */
    private String makeRequestUrl(){
        LocalDateTime today = LocalDateTime.now();

        String API_KEY = "Hg71MQH6djqU7ZssBBaWNXlZZBGmsIrl%2Ba%2FGMjg3WZnCBIXwEs8eR%2BqIxmYTNsPKvB1hV7oM9KZM6mi7uwHNcg%3D%3D";

        String todayString = today.toString();
        String baseDate = todayString.substring(0,4) + todayString.substring(5, 7) + todayString.substring(8, 10);
        String currentTime = todayString.substring(11, 13) + todayString.substring(14, 16) + todayString.substring(17, 19);

        String[] base = getBaseTime(baseDate, currentTime);
        baseDate = base[0];
        String baseTime = base[1];


        String url = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

        String uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("serviceKey", API_KEY)
                .queryParam("numOfRows", 10)
                .queryParam("pageNo", 1)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", 61)
                .queryParam("ny", 131)
                .build(true)
                .toUriString();

        return uri;
    }


    /**
     * 가장 가까운 baseTime을 찾고, 없으면 전날의 23:00을 반환하는 메서드
     * @param baseDate 기준 날짜
     * @param currentTime 현재 시간 (HHmm 형식)
     * @return baseDate, baseTime (배열로 반환)
     */
    private String[] getBaseTime(String baseDate, String currentTime) {
        // 단기예보 baseTime: 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300
        String[] baseTimes = {"0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"};

        // 현재 시간을 정수형으로 변환
        int currentHour = Integer.parseInt(currentTime.substring(0, 2));
        int currentMinute = Integer.parseInt(currentTime.substring(2, 4));

        // 가장 가까운 baseTime을 찾기
        String baseTime = null;
        for (String time : baseTimes) {
            int baseHour = Integer.parseInt(time.substring(0, 2));
            // baseTime 기준 10분 이후부터 조회 가능
            if (currentHour > baseHour || (currentHour == baseHour && currentMinute >= 10)) {
                baseTime = time;
                break;
            }
        }

        // 만약 찾지 못했다면, 가장 마지막 시간(230000)을 사용하고, 날짜를 하루 전으로 변경
        if (baseTime == null) {
            baseTime = baseTimes[baseTimes.length - 1]; // 230000
            // 날짜를 하루 전으로 변경
            baseDate = getYesterdayDate(baseDate);
        }

        return new String[] {baseDate, baseTime};
    }

    /**
     * 주어진 날짜를 하루 전으로 변경하는 메서드
     * @param baseDate 기준 날짜 (yyyyMMdd String)
     * @return 하루 전 날짜 (yyyyMMdd String)
     */
    private String getYesterdayDate(String baseDate) {
        // 날짜 문자열을 LocalDate로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(baseDate, formatter);

        // 하루 전 날짜를 YYYYMMDD 형태의 문자열로 반환
        LocalDate yesterday = date.minusDays(1);
        return yesterday.format(formatter);
    }
}

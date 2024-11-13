package com.bigs.controller;

import com.bigs.service.SyncService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SyncController {
    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping(value = "/forecast", produces = "application/json")
    public ResponseEntity<String> testApi() {
        try {
            syncService.fetchAndSaveForecastData();
            return ResponseEntity.status(HttpStatus.OK).body("단기 예보 DB에 저장 성공.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("단기 예보 저장 실패: " + e.getMessage());
        }
    }
}

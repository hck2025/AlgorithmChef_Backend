package com.webservice.algorithmchef.controller;

import com.webservice.algorithmchef.service.SpeechService;
import lombok.extern.slf4j.Slf4j; // [1] Lombok 로깅 어노테이션
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j // [2] 로그 기능 활성화
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class SttController {

    private final SpeechService speechService;

    public SttController(SpeechService speechService) {
        this.speechService = speechService;
    }

    @PostMapping("/stt")
    public ResponseEntity<Map<String, Object>> stt(
            @RequestParam("audio") MultipartFile audioFile,
            @RequestParam("userId") String userId
    ) {
        log.info("STT 요청 수신 - UserId: {}, 파일크기: {} bytes", userId, audioFile.getSize());

        try {
            String text = speechService.stt(audioFile);

            Map<String, Object> body = new HashMap<>();
            body.put("userId", userId);
            body.put("text", (text == null || text.isEmpty()) ? "stt fail" : text);

            log.info("STT 변환 성공 - UserId: {}", userId);

            return ResponseEntity.ok(body);

        } catch (Exception e) {
            log.error("STT 변환 중 오류 발생 - UserId: {}", userId, e);

            Map<String, Object> body = new HashMap<>();
            body.put("userId", userId);
            body.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }
}

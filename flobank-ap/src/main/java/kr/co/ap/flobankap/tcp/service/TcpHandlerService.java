// TCP 요청/응답 처리기
package kr.co.ap.flobankap.tcp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.ap.flobankap.dto.ApRequestDTO;
import kr.co.ap.flobankap.dto.ApResponseDTO;
import kr.co.ap.flobankap.service.RequestRouterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class TcpHandlerService {

    private final ObjectMapper objectMapper; // JSON <-> DTO 변환기
    private final RequestRouterService requestRouterService;

    /**
     * @ServiceActivator: "tcpRequestChannel"에 메시지가 도착하면 이 메서드를 실행
     *
     * @param requestPayload : 'ByteArrayLfSerializer'에 의해 변환된 byte[] (JSON 문자열)
     * @return : 클라이언트(flobank_api)에게 다시 보낼 byte[] (JSON 문자열)
     */
    @ServiceActivator(inputChannel = "tcpRequestChannel")
    public byte[] handleTcpRequest(byte[] requestPayload) {

        String jsonRequest = "";

        try {
            // 1. (역직렬화) byte[] -> String -> ApRequestDTO
            jsonRequest = new String(requestPayload, StandardCharsets.UTF_8);
            log.info("[TCP RECV] : {}", jsonRequest); // 수신 로그

            ApRequestDTO requestDTO = objectMapper.readValue(jsonRequest, ApRequestDTO.class);

            // 2. (비즈니스 로직) 실제 서비스 호출
            // (예시: ApRequestDTO의 '요청 코드'에 따라 다른 서비스 메서드 호출)
            ApResponseDTO responseDTO = exchangeService.processRequest(requestDTO); // 이 메서드를 ExchangeService에 구현해야 함

            // 3. (직렬화) ApResponseDTO -> String -> byte[]
            String jsonResponse = objectMapper.writeValueAsString(responseDTO);
            log.info("[TCP SEND] : {}", jsonResponse); // 발신 로그

            return jsonResponse.getBytes(StandardCharsets.UTF_8);

        } catch (Exception e) {
            // 4. (예외 처리) DTO 변환 실패 또는 로직 수행 중 에러 발생 시
            log.error("[TCP ERROR] : {}, Request: {}", e.getMessage(), jsonRequest);

            // 클라이언트가 무한정 기다리지 않도록 반드시 에러 응답을 보냅니다.
            ApResponseDTO errorResponse = new ApResponseDTO("ERROR", e.getMessage(), null); // (예시 DTO 구조)

            try {
                String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);
                return jsonErrorResponse.getBytes(StandardCharsets.UTF_8);
            } catch (Exception ex) {
                // 에러 응답조차 JSON으로 만들 수 없는 최악의 경우
                return "{\"status\":\"ERROR\",\"message\":\"Critical server error\"}".getBytes(StandardCharsets.UTF_8);
            }
        }
    }
}
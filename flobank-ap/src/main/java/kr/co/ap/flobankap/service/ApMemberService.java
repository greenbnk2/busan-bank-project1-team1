package kr.co.ap.flobankap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // LocalDate 파싱용
import kr.co.ap.flobankap.dto.MemberDTO;
import kr.co.ap.flobankap.dto.ApResponseDTO;
import kr.co.ap.flobankap.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApMemberService {

    private final MemberMapper memberMapper;

    // ObjectMapper는 기본적으로 LocalDate를 파싱하지 못하므로, JavaTimeModule을 등록해야 합니다.
    // (또는 DTO에서 String으로 받고 여기서 파싱)
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * [핵심] 회원가입 요청을 처리하고 고객 코드를 생성합니다.
     */
    @Transactional // DB에 INSERT가 있으므로 트랜잭션 처리
    public ApResponseDTO registerMember(JsonNode payload) {
        try {
            // 1. API 서버가 보낸 JsonNode(payload)를 AP 서버의 MemberDTO로 변환
            // (API의 MemberDTO(카멜) -> AP의 MemberDTO(카멜) 필드명이 일치해야 함)
            MemberDTO memberDTO = objectMapper.treeToValue(payload, MemberDTO.class);

            // 2. 고객 코드 생성 (연도2-고객구분2-시퀀스4)
            String newCustCode = generateCustCode();
            memberDTO.setCustCode(newCustCode);

            // 3. 서버에서 생성할 기본값 설정
            memberDTO.setCustRegDt(LocalDate.now()); // 가입일 (DB SYSDATE 대신 설정)
            memberDTO.setCustStatus(1);              // 회원 상태 (1: 정상)

            // 4. DB에 INSERT
            int result = memberMapper.insertMember(memberDTO);
            if (result == 0) {
                throw new Exception("회원 정보 저장에 실패했습니다.");
            }

            log.info("[회원가입 성공] 신규 고객 코드: {}", newCustCode);

            // 5. 성공 응답 반환
            return new ApResponseDTO("SUCCESS", "회원가입이 완료되었습니다.", null, LocalDateTime.now());

        } catch (Exception e) {
            log.error("[회원가입 실패] Error: {}", e.getMessage(), e);
            // TODO: DB 중복 키(PK_TB_CUST_INFO) 에러(ex: ORA-00001)인지 확인하여
            // "이미 사용중인 아이디입니다." 같은 구체적인 메시지를 반환하면 더 좋습니다.
            return new ApResponseDTO("ERROR", "회원가입 처리 중 오류 발생: " + e.getMessage(), null, LocalDateTime.now());
        }
    }

    /**
     * 고객 코드 (CHAR 8) 생성
     * @return YY(연도2) + CC(고객구분2) + SSSS(시퀀스4)
     */
    private String generateCustCode() {
        // 1. 연도 2자리 (YY)
        String year = LocalDate.now().format(DateTimeFormatter.ofPattern("yy")); // "25"

        // 2. 고객 구분 2자리 (CC) - (예: 개인고객 '01')
        String custType = "01";

        // 3. 시퀀스 4자리 (SSSS)
        // DB에서 시퀀스 가져오기
        int nextSeq = memberMapper.getNextCustSequence(); // 1

        // 1 -> "0001", 123 -> "0123"
        String sequence = String.format("%04d", nextSeq);

        // 4. 조합: "25" + "01" + "0001" = "25010001"
        return year + custType + sequence;
    }
}
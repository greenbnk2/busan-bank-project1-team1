package kr.co.api.flobankapi.service;

import kr.co.api.flobankapi.dto.ApResponseDTO;
import kr.co.api.flobankapi.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.crypto.password.PasswordEncoder; // [제거]
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor // ApRequestService만 주입됩니다.
public class MemberService {

    // AP 서버 공용 통신 모듈
    private final ApRequestService apRequestService;

    /**
     * 회원가입을 처리
     */
    public ApResponseDTO register(MemberDTO memberDTO) {

        log.info("[회원가입 요청] DTO 전송: {}", memberDTO.getCustId());

        // 2. [공통 모듈 호출] "MEMBER_REGISTER"라는 요청 코드로 AP 서버에 전송
        ApResponseDTO response = apRequestService.execute("MEMBER_REGISTER", memberDTO);

        // 3. AP 서버의 응답을 컨트롤러로 그대로 반환
        return response;
    }
}
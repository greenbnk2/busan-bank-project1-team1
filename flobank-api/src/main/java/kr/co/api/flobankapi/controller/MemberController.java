package kr.co.api.flobankapi.controller;

import kr.co.api.flobankapi.dto.MemberDTO;
import kr.co.api.flobankapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    final MemberService memberService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/register"; // templates/member/register.html
    }

    /**
     * 회원가입 처리 (POST)
     * th:object="${memberDTO}"로 보낸 폼 데이터를 @ModelAttribute MemberDTO memberDTO로 받습니다.
     */
    @PostMapping("/register")
    public String registerProcess(@ModelAttribute MemberDTO memberDTO) {

        // 1. DTO가 폼 데이터를 제대로 받았는지 로그로 확인
        log.info("[회원가입 시도] : {}", memberDTO.toString());

        // 2. TODO: MemberService를 호출하여 이 DTO를 TCP로 AP 서버에 전송
        memberService.register(memberDTO);

        // 3. 회원가입 완료 페이지로 리다이렉트
        return "redirect:/member/complete"; // TODO: 완료 페이지 컨트롤러 필요
    }
}

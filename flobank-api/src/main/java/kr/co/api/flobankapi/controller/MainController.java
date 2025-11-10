package kr.co.api.flobankapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    /**
     * 메인 페이지
     */
    @GetMapping("/")
    public String mainPage() {
        return "index"; // templates/index.html
    }
}
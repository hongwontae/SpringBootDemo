package org.zerock.b52.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
// MemberController (”/member”)
@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/member")
public class MemberController {

    // "/signup" ⇒ permitAll
    @PreAuthorize("permitAll")
    @GetMapping("/signup")
    public void signup(){
        log.info("signup----------------");
    }

    // "/mypage" ⇒ hasRole(’USER’)
    // ROLE_은 빼고 써야된다 
    // 인증이 안되있다면 => loginPage로
    // 인가가 안되있다면 => 403 forbidden
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mypage")
    public void mypage(){
        log.info("mypage----------");
    }

    // "/signin" => permitAll
    @PreAuthorize("permitAll")
    @GetMapping("/signin")
    public void signin(){
        log.info("signin----------------");
    }
}

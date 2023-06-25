package org.zerock.b5.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {
    

    @PreAuthorize("permitAll")
    @GetMapping("/all")
    public void doAll(){
        log.info("doAll............");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public void doUser(){
        log.info("doUser............");
    }
    @PreAuthorize("hasRole('G2')")
    @GetMapping("/g1")
    public void doG1(){
        log.info("doG1............");
    }
}

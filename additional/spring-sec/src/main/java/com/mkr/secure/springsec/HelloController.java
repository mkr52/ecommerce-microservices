package com.mkr.secure.springsec;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String greeting() {
        return "Hello World!";
    }

    @GetMapping("/contact")
    public String contact() {
        return "Contact World!";
    }
}

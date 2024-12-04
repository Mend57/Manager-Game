package com.fm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MenuController {
    @GetMapping("/")
    public String home() {
        return "index";
    }


}

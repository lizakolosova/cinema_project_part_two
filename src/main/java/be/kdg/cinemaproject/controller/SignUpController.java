package be.kdg.cinemaproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignUpController {

    @GetMapping("/register")
    public String register() {
        return "signup";
    }
}

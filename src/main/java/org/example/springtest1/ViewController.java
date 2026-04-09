package org.example.springtest1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";  // Повертає назву HTML файлу (без .html)
    }
}

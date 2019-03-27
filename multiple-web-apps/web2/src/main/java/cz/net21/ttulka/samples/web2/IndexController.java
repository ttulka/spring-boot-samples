package cz.net21.ttulka.samples.web2;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
class IndexController {

    @GetMapping("/")
    public String list(String productName, Map<String, Object> model) {
        return "index";
    }
}

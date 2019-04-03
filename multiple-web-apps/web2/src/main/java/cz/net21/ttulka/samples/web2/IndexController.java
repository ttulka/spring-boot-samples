package cz.net21.ttulka.samples.web2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class IndexController {

    @GetMapping
    public String index() {
        return "index";
    }
}

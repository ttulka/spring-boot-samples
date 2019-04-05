package com.example.samples.webidx;

import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller("webidxIndexController")
@RequestMapping("${path.root:}")
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final String pathRoot;
    private final Collection<String> paths;

    @GetMapping
    public String index(Model model) {
        log.info("INDEX WEBIDX: {}", paths);
        model.addAttribute("pathRoot", pathRoot);
        model.addAttribute("paths", paths);
        return "webidx/index";
    }
}

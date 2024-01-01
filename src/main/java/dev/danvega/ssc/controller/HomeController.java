package dev.danvega.ssc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String home() {
        return "Hello, World!";
    }

    @GetMapping("/private")
    public String secure() {
        return "secured";
    }

    @GetMapping("/hello")
    public String login() {
        LOG.info("hello page");
        return "hello";
    }


}

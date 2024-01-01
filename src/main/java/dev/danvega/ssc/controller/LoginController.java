package dev.danvega.ssc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller
public class LoginController {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

   // @GetMapping("/login1")
    public String getApiLogin1() {
        LOG.info("returning api/login 1 template page name");
        return "login1";
    }

    //@GetMapping("/login2")
    public String getApiLogin2() {
        LOG.info("returning api/login2 template page name");
        return "login2";
    }
}

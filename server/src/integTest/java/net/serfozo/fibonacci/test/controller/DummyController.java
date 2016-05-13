package net.serfozo.fibonacci.test.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dummy")
public class DummyController {
    @RequestMapping(method = RequestMethod.GET)
    public String getDummy() {
        return "dummy";
    }

    @RequestMapping(value = "/sec", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String getDummySec() {
        return "dummysec";
    }
}

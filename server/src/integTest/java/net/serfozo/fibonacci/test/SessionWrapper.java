package net.serfozo.fibonacci.test;

import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;

public class SessionWrapper extends MockHttpSession {
    private final HttpSession httpSession;

    public SessionWrapper(HttpSession httpSession){
        this.httpSession = httpSession;
    }

    @Override
    public Object getAttribute(String name) {
        return this.httpSession.getAttribute(name);
    }

}

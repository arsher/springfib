package net.serfozo.fibonacci.web;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import net.serfozo.fibonacci.test.AbstractWebIntegrationTest;
import net.serfozo.fibonacci.test.SessionHolder;
import net.serfozo.fibonacci.test.SessionWrapper;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static net.serfozo.fibonacci.web.Constants.FIBONACCI_NUMBER_PARAM;
import static net.serfozo.fibonacci.web.Constants.FIBONACCI_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DatabaseSetup("/FibonacciControllerIntegrationTest.yaml")
public class FibonacciControllerIntegrationTest extends AbstractWebIntegrationTest {
    @Test
    public void thatAnonymousUnauthorized() throws Exception {
        final MockMvc mockMvc = getMockMvc();
        mockMvc.perform(get(FIBONACCI_URL).param(FIBONACCI_NUMBER_PARAM, "4"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void thatAuthorizedWorks() throws Exception {
        final MockMvc mockMvc = getMockMvc();
        final SessionHolder sessionHolder = new SessionHolder();
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "testuser")
                .param("password", "testpassword")
        )
        .andExpect(status().isNoContent())
        .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));

        mockMvc.perform(get(FIBONACCI_URL).param(FIBONACCI_NUMBER_PARAM, "4")
                    .session(sessionHolder.getSession()))
                .andExpect(status().isOk())
                .andExpect(content().json("[1,1,2,3]"));
    }

    @Test
    public void thatErrorReturnedForInvalidParam() throws Exception {
        final MockMvc mockMvc = getMockMvc();
        final SessionHolder sessionHolder = new SessionHolder();

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "testuser")
                .param("password", "testpassword")
        )
                .andExpect(status().isNoContent())
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));

        mockMvc.perform(get(FIBONACCI_URL)
                    .session(sessionHolder.getSession())
                    .param(FIBONACCI_NUMBER_PARAM, "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{message:\"number must be greater than or equal to zero\"}"));
    }

    @Test
    public void thatErrorReturnedForTooBigParam() throws Exception {
        final MockMvc mockMvc = getMockMvc();
        final SessionHolder sessionHolder = new SessionHolder();

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "testuser2")
                .param("password", "testpassword")
        )
                .andExpect(status().isNoContent())
                .andDo(result -> sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession())));

        mockMvc.perform(get(FIBONACCI_URL)
                .session(sessionHolder.getSession())
                .param(FIBONACCI_NUMBER_PARAM, "4"))
                .andExpect(status().isForbidden())
                .andExpect(content().json("{message:\"The current user is not authorized to use that number as parameter.\"}"));
    }
}

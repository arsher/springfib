package net.serfozo.fibonacci.config;

import net.serfozo.fibonacci.test.AbstractWebIntegrationTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityConfigIntegrationTest extends AbstractWebIntegrationTest {
    @Test
    public void thatWrongLoginIsUnauthorized() throws Exception {
        final MockMvc mockMvc = getMockMvc();

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "testuser")
                .param("password", "testpassword"))
                .andExpect(status().isUnauthorized());
    }
}

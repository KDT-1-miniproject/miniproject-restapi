package shop.mtcoding.miniproject2.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.miniproject2.model.User;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class CompanyScrapController {

    @Autowired
    private MockHttpSession mockSession;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(3);
        user.setPassword("ad38f305434fb803fbadb9cf57df1e822bff382352c19dc67b5b13055a049cd6");
        user.setEmail("init@nate.com");
        user.setSalt("cat");
        user.setPInfoId(0);
        user.setCInfoId(1);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }

    @Test
    public void scrapInsert_test() throws Exception {
        int resumeId = 2;
        // String requestBody = "id=" + resumeId;

        // ResultActions resultActions = mvc
        // .perform(post("/company/scrap/" +
        // resumeId).content(requestBody).session(mockSession)
        // .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        ResultActions resultActions = mvc
                .perform(post("/company/scrap/" + resumeId).session(mockSession));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void scrapDelete_test() throws Exception {
        int resumeId = 1;

        ResultActions resultActions = mvc
                .perform(delete("/company/scrap/" + resumeId).session(mockSession));

        resultActions.andExpect(status().isOk());
    }
}

package shop.mtcoding.miniproject2.controller.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.miniproject2.model.User;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class PersonProposalControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private MockHttpSession mockSession;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(1);
        user.setPassword("9d85d697da8136003c67ea366b8c6a0225cb0f3ff95aca3e4634f0e09a8e6723");
        user.setEmail("ssar@nate.com");
        user.setSalt("bear");
        user.setPInfoId(1);
        user.setCInfoId(0);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }

    @Test
    public void resumeSubmitTest() throws Exception {
        // given
        int id = 4; // postId, url
        String requestBody = "selectedResume=1"; // x-www-form-urlencoded : key&value
        // when
        ResultActions resultActions = mvc.perform(post("/person/detail/" + id + "/resume").content(requestBody)
                .session(mockSession).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        HttpSession session = resultActions.andReturn().getRequest().getSession();
        User principal = (User) session.getAttribute("principal");

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.print("테스트: " + responseBody);
        // then
        assertThat(principal.getPInfoId()).isEqualTo(1);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void resumeSubmittedList_test() throws Exception {

        ResultActions resultActions = mvc
                .perform(get("/person/history").session(mockSession));

        resultActions.andExpect(jsonPath("$.data").exists());
        resultActions.andExpect(jsonPath("$.msg").value("지원이력보기"));
    }
}

package shop.mtcoding.miniproject2.controller.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.miniproject2.dto.user.UserLoginDto;
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

    public String jwt() {
        String jwt = JWT
                .create()
                .withSubject("principal")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .withClaim("id", 1) // user의 primary key
                .withClaim("cInfoId", 0)
                .withClaim("pInfoId", 1)
                .withClaim("email", "init@nate.com")
                .sign(Algorithm.HMAC512(System.getenv("project_secret")));
        return jwt;
    }

    @BeforeEach // Test메서드 실행 직전마다 호출된다
    public void setUp() {
        // 임시 세션 생성하기
        UserLoginDto user = new UserLoginDto();
        user.setId(1);
        user.setEmail("ssar@nate.com");
        user.setPInfoId(1);
        user.setCInfoId(0);

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
                .session(mockSession).header("Authorization", jwt())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.print("테스트: " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void resumeSubmittedList_test() throws Exception {

        ResultActions resultActions = mvc
                .perform(get("/person/history").session(mockSession).header("Authorization", jwt()));

        resultActions.andExpect(jsonPath("$.data").exists());
        resultActions.andExpect(jsonPath("$.msg").value("지원이력보기"));
    }
}

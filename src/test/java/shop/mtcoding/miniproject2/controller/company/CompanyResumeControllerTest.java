package shop.mtcoding.miniproject2.controller.company;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.miniproject2.dto.user.UserLoginDto;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class CompanyResumeControllerTest {

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
                .withClaim("id", 3) // user의 primary key
                .withClaim("cInfoId", 1)
                .withClaim("pInfoId", 0)
                .withClaim("email", "init@nate.com")
                .sign(Algorithm.HMAC512(System.getenv("project_secret")));
        return jwt;
    }

    @BeforeEach // Test메서드 실행 직전마다 호출된다
    public void setUp() {
        // 임시 세션 생성하기
        UserLoginDto user = new UserLoginDto();
        user.setId(3);
        user.setEmail("init@nate.com");
        user.setPInfoId(0);
        user.setCInfoId(1);

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }

    @Test
    public void recommend_test() throws Exception {

        ResultActions resultActions = mvc
                .perform(get("/company/recommend").session(mockSession).header("Authorization", jwt()));

        /*
         * String data = resultActions.andReturn().getResponse().getContentAsString();
         * String jData = om.writeValueAsString(data);
         * DocumentContext jsonContext = JsonPath.parse(data);
         * String rdata = jsonContext.read("$.data").toString();
         * System.out.println("테스트: " + rdata);
         */
        resultActions.andExpect(jsonPath("$.msg").value("기업 인재 추천"));
        resultActions.andExpect(jsonPath("$.data").exists());

    }
}

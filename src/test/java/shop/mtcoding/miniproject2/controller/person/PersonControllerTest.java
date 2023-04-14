package shop.mtcoding.miniproject2.controller.person;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

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

import shop.mtcoding.miniproject2.dto.person.PersonInfoInDto;
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class PersonControllerTest {
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
    public void info_test() throws Exception {
        // given
        ResultActions resultActions = mvc
                .perform(get("/person/info").session(mockSession).header("Authorization", jwt()));

        resultActions.andExpect(jsonPath("$.data").exists());
        resultActions.andExpect(jsonPath("$.msg").value("person info"));

    }

    @Test
    public void updateInfo_test() throws Exception {
        PersonInfoInDto pdto = PersonInfoInDto.builder().name("쌀쌀이")
                .address("부산 남구")
                .birthday("2000-04-10 00:00:00")
                .phone("01000002222")
                .email("ssar@nate.com")
                .skills("Java,Javascript,Html,Flutter")
                .password("1004")
                .originPassword("1234").build();

        String requestBody = om.writeValueAsString(pdto); // json으로

        ResultActions resultActions = mvc.perform(put("/person/info")
                .content(requestBody).session(mockSession).header("Authorization", jwt())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

}

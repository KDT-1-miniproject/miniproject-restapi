package shop.mtcoding.miniproject2.controller.person;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeInsertReqDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeUpdateReqDto;
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class PersonResumeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    private MockHttpSession mockSession;

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
    public void personInsertResumeForm_test() throws Exception {

        // given
        ResumeInsertReqDto resumeInsertReqDto = new ResumeInsertReqDto();
        resumeInsertReqDto.setProfile("12345");
        resumeInsertReqDto.setTitle("title");
        resumeInsertReqDto.setPortfolio("porfolio");
        resumeInsertReqDto.setPublish(true);
        resumeInsertReqDto.setSelfIntro("selfintro");
        resumeInsertReqDto.setSkills("skills");

        // x-www-url-encoded -> Json
        String requestBody = om.writeValueAsString(resumeInsertReqDto);
        ResultActions resultActions = mvc
                .perform(post("/person/resumes")
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .session(mockSession).header("Authorization", jwt()));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 :" + responseBody);
        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void personUpdateResumeForm_test() throws Exception {

        // given
        ResumeUpdateReqDto resumeUpdateReqDto = new ResumeUpdateReqDto();
        resumeUpdateReqDto.setProfile("12345");
        resumeUpdateReqDto.setTitle("title");
        resumeUpdateReqDto.setPortfolio("porfolio");
        resumeUpdateReqDto.setPublish(true);
        resumeUpdateReqDto.setSelfIntro("selfintro");
        resumeUpdateReqDto.setSkills("skills");

        // x-www-url-encoded -> Json
        String requestBody = om.writeValueAsString(resumeUpdateReqDto);
        ResultActions resultActions = mvc
                .perform(put("/person/resumes/1")
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .session(mockSession).header("Authorization", jwt()));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 :" + responseBody);
        // then
        resultActions.andExpect(status().isOk());
    }
}

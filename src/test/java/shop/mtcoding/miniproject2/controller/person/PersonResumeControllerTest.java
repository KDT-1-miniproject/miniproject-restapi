package shop.mtcoding.miniproject2.controller.person;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeInsertReqDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeUpdateReqDto;
import shop.mtcoding.miniproject2.model.User;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class PersonResumeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    private MockHttpSession mockSession;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(1);
        user.setPassword("9d85d697da8136003c67ea366b8c6a0225cb0f3ff95aca3e4634f0e09a8e6723");
        user.setSalt("bear");
        user.setEmail("ssar@nate.com");
        user.setPInfoId(1);
        user.setCInfoId(0);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

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
                        .session(mockSession));
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
                        .session(mockSession));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 :" + responseBody);
        // then
        resultActions.andExpect(status().isOk());
    }
}

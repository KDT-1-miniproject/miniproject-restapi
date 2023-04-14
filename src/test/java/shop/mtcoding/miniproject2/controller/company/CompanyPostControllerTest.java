package shop.mtcoding.miniproject2.controller.company;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import shop.mtcoding.miniproject2.dto.post.PostReq.PostSaveReqDto;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostUpdateReqDto;
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class CompanyPostControllerTest {
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
    public void insertPost_test() throws Exception {
        // given

        PostSaveReqDto postSaveReqDto = new PostSaveReqDto();
        postSaveReqDto.setTitle("title");
        postSaveReqDto.setCareer("1");
        postSaveReqDto.setPay("1won");
        postSaveReqDto.setCondition("intern");
        postSaveReqDto.setStartHour("11:00:00");
        postSaveReqDto.setEndHour("15:00:00");
        postSaveReqDto.setDeadline("2023-03-30");
        postSaveReqDto.setComIntro("hi");
        postSaveReqDto.setJobIntro("hello?");
        String[] skill = { "Java", "Sql" };
        postSaveReqDto.setSkills(skill);

        // when
        String requestBody = om.writeValueAsString(postSaveReqDto);
        ResultActions resultActions = mvc
                .perform(post("/company/posts")
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON).header("Authorization", jwt())
                        .session(mockSession));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 :" + responseBody);
        // then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updatePost_test() throws Exception {
        // given
        int id = 1;
        PostUpdateReqDto postUpdateReqDto = new PostUpdateReqDto();
        postUpdateReqDto.setTitle("title");
        postUpdateReqDto.setCareer("1");
        postUpdateReqDto.setPay("1won");
        postUpdateReqDto.setCondition("intern");
        postUpdateReqDto.setStartHour("11:00:00");
        postUpdateReqDto.setEndHour("15:00:00");
        postUpdateReqDto.setDeadline("2023-03-23");
        postUpdateReqDto.setComIntro("hi");
        postUpdateReqDto.setJobIntro("hello?");
        postUpdateReqDto.setSkills("java,sql,html");

        // when
        String requestBody = om.writeValueAsString(postUpdateReqDto);
        ResultActions resultActions = mvc
                .perform(put("/company/posts/" + id)
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .session(mockSession).header("Authorization", jwt()));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 :" + responseBody);
        // then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deletePost_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(delete("/company/posts/" + id)
                .session(mockSession).header("Authorization", jwt()));

        // then
        resultActions.andExpect(jsonPath("$.msg").value("공고 삭제 성공"));
        resultActions.andExpect(status().isOk());
    }
}

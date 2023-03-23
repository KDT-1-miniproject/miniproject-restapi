package shop.mtcoding.miniproject2.controller.company;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import shop.mtcoding.miniproject2.dto.post.PostReq.PostSaveReqDto;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostUpdateReqDto;
import shop.mtcoding.miniproject2.model.User;

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
    public void insertPost_test() throws Exception {
        // given

        PostSaveReqDto postSaveReqDto = new PostSaveReqDto();
        postSaveReqDto.setTitle("title");
        postSaveReqDto.setCareer(1);
        postSaveReqDto.setPay("1won");
        postSaveReqDto.setCondition("intern");
        postSaveReqDto.setStartHour("11:00:00");
        postSaveReqDto.setEndHour("15:00:00");
        postSaveReqDto.setDeadline("2023-03-30");
        postSaveReqDto.setCIntro("hi");
        postSaveReqDto.setJobIntro("hello?");
        String[] skill = { "Java", "Sql" };
        postSaveReqDto.setSkills(skill);

        // when
        String requestBody = om.writeValueAsString(postSaveReqDto);
        ResultActions resultActions = mvc
                .perform(post("/company/posts")
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON)
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
        postUpdateReqDto.setCareer(1);
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
                        .session(mockSession));
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
                .session(mockSession));

        // then
        resultActions.andExpect(jsonPath("$.msg").value("공고 삭제 성공"));
        resultActions.andExpect(status().isOk());
    }
}

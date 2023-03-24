package shop.mtcoding.miniproject2.controller.company;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.miniproject2.dto.company.CompanyInfoInDto;
import shop.mtcoding.miniproject2.model.User;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class CompanyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MockHttpSession mockSession;

    @Autowired
    private ObjectMapper om;

    @BeforeEach // Test메서드 실행 직전마다 호출된다
    public void setUp() {
        // 임시 세션 생성하기
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
    public void companyUpdateInfo_test() throws Exception {
        // given

        CompanyInfoInDto cdto = CompanyInfoInDto.builder().address("부산 남구 00동").bossName("sj")
                .cyear("2000-10-10").logo("haha").managerName("jhs").managerPhone("01099998888").size(120)
                .password("1111").originPassword("1234").build();

        String requestBody = om.writeValueAsString(cdto); // json으로

        // System.out.println("테스트 4:" + requestBody);

        // when
        ResultActions resultActions = mvc.perform(put("/company/info")
                .content(requestBody).session(mockSession).contentType(MediaType.APPLICATION_JSON));

        // then

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void info_test() throws Exception {
        // given
        ResultActions resultActions = mvc.perform(get("/company/info").session(mockSession));

        resultActions.andExpect(jsonPath("$.data").exists());
        resultActions.andExpect(jsonPath("$.msg").value("company info"));

    }
}

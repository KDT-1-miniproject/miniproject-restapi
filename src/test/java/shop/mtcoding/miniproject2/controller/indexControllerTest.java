package shop.mtcoding.miniproject2.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.miniproject2.dto.company.CompanyReq.JoinCompanyReqDto;
import shop.mtcoding.miniproject2.dto.person.PersonReq.JoinPersonReqDto;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class indexControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    public void companyJoin_test() throws Exception {
        // given
        JoinCompanyReqDto joinCompanyReqDto = new JoinCompanyReqDto();
        joinCompanyReqDto.setName("name");
        joinCompanyReqDto.setAddress("address");
        joinCompanyReqDto.setNumber("0123456789");
        joinCompanyReqDto.setManagerName("manager");
        joinCompanyReqDto.setEmail("email@naver.com");
        joinCompanyReqDto.setPassword("password");

        String requestBody = om.writeValueAsString(joinCompanyReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/companyJoin").content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void personJoin_test() throws Exception {
        // given
        JoinPersonReqDto joinPersonReqDto = new JoinPersonReqDto();
        joinPersonReqDto.setName("name");
        joinPersonReqDto.setEmail("email@naver.com");
        joinPersonReqDto.setPassword("password");
        joinPersonReqDto.setSkills("Java");

        String requestBody = om.writeValueAsString(joinPersonReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/personJoin").content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

}

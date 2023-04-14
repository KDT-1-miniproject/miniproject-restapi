package shop.mtcoding.miniproject2.controller.company;

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

import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalReq.CompanyProposalStatusReqDto;
import shop.mtcoding.miniproject2.dto.proposalPass.ProposalPassReq.ProposalPassMessageReqDto;
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class CompanyProposalControllerTest {
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
    public void proposal_test() throws Exception {
        // given
        int id = 4;
        CompanyProposalStatusReqDto status = new CompanyProposalStatusReqDto();
        status.setStatusCode("1");

        String requestBody = om.writeValueAsString(status);
        // when
        ResultActions resultActions = mvc.perform(put("/company/proposal/" + id).content(requestBody)
                .contentType(MediaType.APPLICATION_JSON).session(mockSession).header("Authorization", jwt()));
        // then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void proposalSubmitMsg_test() throws Exception {
        // given
        int id = 4;
        ProposalPassMessageReqDto proposalPassMessageReqDto = new ProposalPassMessageReqDto();
        proposalPassMessageReqDto.setMessage("합격입니다");

        String requestBody = om.writeValueAsString(proposalPassMessageReqDto);
        // when
        ResultActions resultActions = mvc.perform(post("/company/proposalPass/" + id).content(requestBody)
                .contentType(MediaType.APPLICATION_JSON).session(mockSession).header("Authorization", jwt()));
        // then
        resultActions.andExpect(status().is2xxSuccessful());
    }
}

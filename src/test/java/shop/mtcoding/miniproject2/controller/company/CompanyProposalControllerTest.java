package shop.mtcoding.miniproject2.controller.company;

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

import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalReq.CompanyProposalStatusReqDto;
import shop.mtcoding.miniproject2.dto.proposalPass.ProposalPassReq.ProposalPassMessageReqDto;
import shop.mtcoding.miniproject2.model.User;

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
    public void proposal_test() throws Exception {
        // given
        int id = 4;
        CompanyProposalStatusReqDto status = new CompanyProposalStatusReqDto();
        status.setStatusCode("1");

        String requestBody = om.writeValueAsString(status);
        // when
        ResultActions resultActions = mvc.perform(put("/company/proposal/" + id).content(requestBody)
                .contentType(MediaType.APPLICATION_JSON).session(mockSession));
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
                .contentType(MediaType.APPLICATION_JSON).session(mockSession));
        // then
        resultActions.andExpect(status().is2xxSuccessful());
    }
}

package shop.mtcoding.miniproject2.controller.company;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalReq.CompanyProposalStatusReqDto;
import shop.mtcoding.miniproject2.dto.proposalPass.ProposalPassReq.ProposalPassMessageReqDto;
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;
import shop.mtcoding.miniproject2.model.PersonProposal;
import shop.mtcoding.miniproject2.model.ProposalPass;
import shop.mtcoding.miniproject2.service.PersonProposalService;
import shop.mtcoding.miniproject2.service.ProposalPassService;

@RequestMapping("/company")
@RequiredArgsConstructor
@RestController
public class CompanyProposalController {
    private final HttpSession session;
    private final PersonProposalService personProposalService;
    private final ProposalPassService proposalPassService;

    // 합불
    @PutMapping("/proposal/{id}")
    public @ResponseBody ResponseEntity<?> companyUpdateResume(@PathVariable int id,
            @Valid @RequestBody CompanyProposalStatusReqDto statusCode, BindingResult bindingResult) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        PersonProposal dto = personProposalService.제안수정하기(id, principal.getCInfoId(),
                Integer.parseInt(statusCode.getStatusCode()));

        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 확인 완료", dto), HttpStatus.OK);
    }

    // 합격 시 메세지
    @PostMapping("proposalPass/{id}")
    public @ResponseBody ResponseEntity<?> insertProposalPass(@PathVariable int id,
            @Valid @RequestBody ProposalPassMessageReqDto message,BindingResult bindingResult) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        ProposalPass dto = proposalPassService.메시지전달하기(id, principal.getCInfoId(), message.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(1, "메시지 전달 성공", dto), HttpStatus.CREATED);
    }
}

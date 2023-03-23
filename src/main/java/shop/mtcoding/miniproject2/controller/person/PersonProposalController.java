package shop.mtcoding.miniproject2.controller.person;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.PersonProposalListRespDto;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.PersonProposal;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.service.PersonProposalService;

@RequestMapping("/person")
@RequiredArgsConstructor
@RestController
public class PersonProposalController {

    private final HttpSession session;
    private final PersonProposalService personProposalService;

    @PostMapping("/detail/{id}/resume")
    public ResponseEntity<?> resumeSubmit(@PathVariable("id") int id, int selectedResume) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        // post아이디는 여기 id! + resumeid는 int selectedResume
        PersonProposal dto = personProposalService.지원하기(principal.getPInfoId(), id, selectedResume); // status 합불합격상태(0은
                                                                                                     // 대기중)
        return new ResponseEntity<>(new ResponseDto<>(1, "제안 성공", dto), HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<?> resumeDetail() {

        User principalPS = (User) session.getAttribute("principal");

        List<PersonProposalListRespDto> dto = personProposalService.지원이력보기(principalPS.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "지원이력보기", dto), HttpStatus.OK);
    }
}

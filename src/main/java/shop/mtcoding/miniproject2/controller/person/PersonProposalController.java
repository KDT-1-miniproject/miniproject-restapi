package shop.mtcoding.miniproject2.controller.person;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.PersonProposalStringListRespDto;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.PersonProposalRepository;
import shop.mtcoding.miniproject2.model.ProposalPass;
import shop.mtcoding.miniproject2.model.ProposalPassRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.service.PersonProposalService;

@RequestMapping("/person")
@RequiredArgsConstructor
@RestController
public class PersonProposalController {

    private final HttpSession session;
    private final PersonProposalRepository personProposalRepository;
    private final ProposalPassRepository proposalPassRepository;
    private final PersonProposalService personProposalService;

    @PostMapping("/detail/{id}/resume")
    public ResponseEntity<?> resumeSubmit(@PathVariable("id") int id, int selectedResume) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        int pInfoId = principal.getPInfoId();
        // post아이디는 여기 id! + resumeid는 int selectedResume
        personProposalService.지원하기(pInfoId, id, selectedResume, 0); // status 합불합격상태(0은 대기중)

        return new ResponseEntity<>(new ResponseDto<>(1, "제안 성공", null), HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<?> resumeDetail() {

        User principalPS = (User) session.getAttribute("principal");
        List<PersonProposalListRespDto> personProposalList = personProposalRepository
                .findAllWithPostAndCInfoByPInfoId(principalPS.getPInfoId());

        List<ProposalPass> proposalPassList = proposalPassRepository.findAllByPInfoId(principalPS.getPInfoId());
        if (proposalPassList.size() > 0) {
            // model.addAttribute("proposalPassList", proposalPassList);

        }
        List<PersonProposalStringListRespDto> personProposalList2 = new ArrayList<>();

        for (PersonProposalListRespDto pp : personProposalList) {
            Timestamp deadline = pp.getDeadline();
            Date date = new Date(deadline.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDeadline = sdf.format(date);
            PersonProposalStringListRespDto dto = new PersonProposalStringListRespDto();

            dto.setId(pp.getId());
            dto.setCreatedAt(pp.getCreatedAt());
            dto.setDeadline(formattedDeadline);
            dto.setName(pp.getName());
            dto.setPInfoId(pp.getPInfoId());
            dto.setPostId(pp.getPInfoId());
            dto.setResumeId(pp.getResumeId());
            dto.setStatus(pp.getStatus());
            dto.setTitle(pp.getTitle());
            personProposalList2.add(dto);
        }
        // model.addAttribute("personProposalList", personProposalList2);

        return new ResponseEntity<>(new ResponseDto<>(1, "", null), HttpStatus.OK);
    }

}

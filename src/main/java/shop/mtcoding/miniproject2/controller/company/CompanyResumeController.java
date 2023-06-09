package shop.mtcoding.miniproject2.controller.company;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeDetailOutDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeWithPostInfoRecommendDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.CompanyGetResumeDto;
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;
import shop.mtcoding.miniproject2.service.CompanyService;
import shop.mtcoding.miniproject2.service.PersonProposalService;

@RequestMapping("/company")
@RequiredArgsConstructor
@RestController
public class CompanyResumeController {
    private final HttpSession session;
    private final PersonProposalService personProposalService;
    private final CompanyService companyService;

    @GetMapping("/resumes")
    public ResponseEntity<?> resume() {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        CompanyGetResumeDto dto = personProposalService.받은이력서보기(principal.getCInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "받은 이력서 보기", dto), HttpStatus.OK);
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<?> resumeDetail(@PathVariable int id) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        ResumeDetailOutDto dto = personProposalService.이력서디테일보기(id, principal.getCInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 디테일 보기 ", dto), HttpStatus.OK);
    }

    // 공고 + 스킬 찾기
    @GetMapping("/recommend")
    public ResponseEntity<?> recommend() {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        List<ResumeWithPostInfoRecommendDto> postWithReumseDto = companyService.recommend();
        return new ResponseEntity<>(new ResponseDto<>(1, "기업 인재 추천", postWithReumseDto), HttpStatus.OK);
    }

}

package shop.mtcoding.miniproject2.controller.company;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.companyScrap.CompanyScrapOutDto;
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;
import shop.mtcoding.miniproject2.model.CompanyScrap;
import shop.mtcoding.miniproject2.model.CompanyScrapRepository;
import shop.mtcoding.miniproject2.service.CompanyScrapService;

@RequestMapping("/company")
@RequiredArgsConstructor
@RestController
public class CompanyScrapController {
    private final HttpSession session;
    private final CompanyScrapService companyScrapService;
    private final CompanyScrapRepository companyScrapRepository;

    @GetMapping("/scrap")
    public ResponseEntity<?> scrap() {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        List<CompanyScrapOutDto> cScrapPS = companyScrapRepository.findByIdResumeAndSkillFilter(principal.getCInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "기업 스크랩 목록", cScrapPS), HttpStatus.OK);
    }

    @DeleteMapping("/scrap/{id}")
    public ResponseEntity<?> scrapDelete(@PathVariable int id) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        companyScrapService.delete(id, principal.getCInfoId());
        return new ResponseEntity<>(new ResponseDto<>(1, "스크랩 취소", null), HttpStatus.OK);
    }

    @PostMapping("/scrap/{id}")
    public ResponseEntity<?> scrapInsert(@PathVariable int id) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        CompanyScrap cScrapPS = companyScrapService.insert(id, principal.getCInfoId());
        return new ResponseEntity<>(new ResponseDto<>(1, "스크랩 완료", cScrapPS), HttpStatus.OK);
    }
}

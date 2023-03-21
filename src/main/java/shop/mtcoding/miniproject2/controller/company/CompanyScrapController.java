package shop.mtcoding.miniproject2.controller.company;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.companyScrap.CompanyScrapOutDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.CompanyScrapRepository;
import shop.mtcoding.miniproject2.model.User;
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
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        List<CompanyScrapOutDto> cScrapPS = companyScrapRepository.findByIdResumeAndSkillFilter(principal.getCInfoId());

        // model.addAttribute("scrapList", cScrapArrList);
        return new ResponseEntity<>(new ResponseDto<>(1, "", cScrapPS), HttpStatus.OK);
    }

    @DeleteMapping("/scrap/{id}")
    public ResponseEntity<?> scrapDelete(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        companyScrapService.delete(id, principal.getCInfoId());
        return new ResponseEntity<>(new ResponseDto<>(1, "스크랩 취소", null), HttpStatus.OK);
    }

    @PutMapping("/scrap/{id}")
    public ResponseEntity<?> scrapInsert(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        companyScrapService.insert(id, principal.getCInfoId());
        return new ResponseEntity<>(new ResponseDto<>(1, "스크랩 완료", null), HttpStatus.OK);
    }
}

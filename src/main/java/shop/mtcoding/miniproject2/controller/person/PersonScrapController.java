package shop.mtcoding.miniproject2.controller.person;

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
import shop.mtcoding.miniproject2.dto.personScrap.PersonScrapOutDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.PersonScrap;
import shop.mtcoding.miniproject2.model.PersonScrapRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.service.PersonScrapService;

@RequestMapping("/person")
@RequiredArgsConstructor
@RestController
public class PersonScrapController {

    private final HttpSession session;
    private final PersonScrapRepository personScrapRepository;
    private final PersonScrapService personScrapService;

    @GetMapping("/scrap")
    public ResponseEntity<?> personScrap() {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        List<PersonScrapOutDto> pScrapPS = personScrapRepository.findByIdWithPostAndCompany(principal.getPInfoId());

        // model.addAttribute("pScrapList", pScrapList2);
        // model.addAttribute("count", pScrapList.size());

        return new ResponseEntity<>(new ResponseDto<>(1, "", pScrapPS),
                HttpStatus.OK);
    }

    @PostMapping("/scrap/{id}")
    public ResponseEntity<?> scrapInsert(@PathVariable int id) {
        // personMocLogin();
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        System.out.println("테스트 : " + id);
        PersonScrap scrap = personScrapService.insert(id, principal.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "스크랩 완료", scrap), HttpStatus.OK);
    }

    @DeleteMapping("/scrap/{id}")
    public ResponseEntity<?> scrapDelete(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        personScrapService.delete(id, principal.getPInfoId());
        return new ResponseEntity<>(new ResponseDto<>(1, "스크랩 취소 완료", null), HttpStatus.OK);
    }
}

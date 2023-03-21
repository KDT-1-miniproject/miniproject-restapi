package shop.mtcoding.miniproject2.controller.person;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.personScrap.PersonScrapResDto.PersonScrapIntegerResDto;
import shop.mtcoding.miniproject2.dto.personScrap.PersonScrapResDto.PersonScrapTimeStampResDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.PersonScrapRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.service.PersonScrapService;
import shop.mtcoding.miniproject2.util.CvTimestamp;

@RequestMapping("/person")
@RequiredArgsConstructor
@RestController
public class PersonScrapController {

    private final HttpSession session;
    private final PersonScrapRepository personScrapRepository;
    private final PersonScrapService personScrapService;

    @GetMapping("/scrap")
    public @ResponseBody ResponseEntity<?> personScrap() {
        // 수정 필요

        User principal = (User) session.getAttribute("principal");
        List<PersonScrapTimeStampResDto> pScrapList = personScrapRepository.findByPInfoId(principal.getPInfoId());

        List<PersonScrapIntegerResDto> pScrapList2 = new ArrayList<>();
        for (PersonScrapTimeStampResDto p : pScrapList) {
            Integer deadline = CvTimestamp.ChangeDDay(p.getDeadline());
            PersonScrapIntegerResDto ps = new PersonScrapIntegerResDto();
            ps.setId(p.getId());
            ps.setPInfoId(p.getPInfoId());
            ps.setPostId(p.getPostId());
            ps.setAddress(p.getAddress());
            ps.setDeadline(deadline);
            ps.setLogo(p.getLogo());
            ps.setName(p.getName());
            ps.setTitle(p.getTitle());
            pScrapList2.add(ps);
        }

        // model.addAttribute("pScrapList", pScrapList2);
        // model.addAttribute("count", pScrapList.size());

        return new ResponseEntity<>(new ResponseDto<>(1, "", null),
                HttpStatus.OK);
    }

    @PutMapping("/scrap/{id}")
    public ResponseEntity<?> scrapInsert(@PathVariable int id) {
        // personMocLogin();
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        personScrapService.insert(id, principal.getPInfoId());
        return new ResponseEntity<>(new ResponseDto<>(1, "스크랩 완료", null), HttpStatus.OK);
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

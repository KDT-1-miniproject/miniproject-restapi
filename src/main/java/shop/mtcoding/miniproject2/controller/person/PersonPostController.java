package shop.mtcoding.miniproject2.controller.person;

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
import shop.mtcoding.miniproject2.dto.post.PostRecommendOutDto.PostRecommendIntegerRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PersonPostDetailResDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostMainRespDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.CompanyRepository;
import shop.mtcoding.miniproject2.model.PersonScrapRepository;
import shop.mtcoding.miniproject2.model.PostRepository;
import shop.mtcoding.miniproject2.model.ResumeRepository;
import shop.mtcoding.miniproject2.model.SkillFilterRepository;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.service.PersonService;
import shop.mtcoding.miniproject2.service.PostService;

@RequestMapping("/person")
@RequiredArgsConstructor
@RestController
public class PersonPostController {
    private final HttpSession session;
    private final ResumeRepository resumeRepository;
    private final PostRepository postRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;
    private final SkillFilterRepository skillFilterRepository;
    private final PersonScrapRepository personScrapRepository;
    private final PostService postService;
    private final PersonService personService;

    @GetMapping({ "/main", "/" })
    public ResponseEntity<?> main() {
        User principal = (User) session.getAttribute("principal");

        // 회사로고, 회사이름, 공고이름, 회사 주소, D-day
        // cInfo : 회사로고, 회사이름, 회사주소
        // 공고 정보 : 공고이름, 디데이
        List<PostMainRespDto> posts = postService.개인공고리스트(principal.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "개인 공고 리스트 보기 ", posts), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable int id) {

        User userPS = (User) session.getAttribute("principal");
        if (userPS == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.FORBIDDEN);
        }

        PersonPostDetailResDto post = postService.개인공고디테일(id, userPS.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "개인 공고 디테일 보기", post), HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public ResponseEntity<?> recommend() {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.FORBIDDEN);
        }
        List<PostRecommendIntegerRespDto> postListDto = personService.recommend();


        return new ResponseEntity<>(new ResponseDto<>(1, "공고 추천", postListDto), HttpStatus.OK);
    }
}

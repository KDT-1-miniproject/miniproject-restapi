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
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;
import shop.mtcoding.miniproject2.service.PersonService;
import shop.mtcoding.miniproject2.service.PostService;

@RequestMapping("/person")
@RequiredArgsConstructor
@RestController
public class PersonPostController {
    private final HttpSession session;
    private final PostService postService;
    private final PersonService personService;

    @GetMapping({ "/main", "/" })
    public ResponseEntity<?> main() {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        // 회사로고, 회사이름, 공고이름, 회사 주소, D-day
        // cInfo : 회사로고, 회사이름, 회사주소
        // 공고 정보 : 공고이름, 디데이
        List<PostMainRespDto> posts = postService.개인공고리스트(principal.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "개인 공고 리스트 보기 ", posts), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable int id) {

        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        PersonPostDetailResDto post = postService.개인공고디테일(id, principal.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "개인 공고 디테일 보기", post), HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public ResponseEntity<?> recommend() {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        List<PostRecommendIntegerRespDto> postListDto = personService.recommend();

        return new ResponseEntity<>(new ResponseDto<>(1, "개인 공고 추천", postListDto), HttpStatus.OK);
    }
}

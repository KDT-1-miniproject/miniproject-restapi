package shop.mtcoding.miniproject2.controller.company;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostSaveDto;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostSaveReqDto;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostUpdateDto;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostUpdateReqDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.CompanyPostDetailRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostTitleRespDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.CompanyRepository;
import shop.mtcoding.miniproject2.model.PostRepository;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.service.PostService;

@RequestMapping("/company")
@RequiredArgsConstructor
@RestController
public class CompanyPostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;
    private final HttpSession session;

    @GetMapping("/posts")
    public ResponseEntity<?> posts() {
        User userPS = (User) session.getAttribute("principal");
        if (userPS == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED);
        }

        List<PostTitleRespDto> postTitleList = postService.기업공고리스트(userPS.getCInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "기업 공고 리스트 보기", postTitleList), HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<?> postDetail(@PathVariable int id) {
        User userPS = (User) session.getAttribute("principal");
        if (userPS == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED);
        }

        CompanyPostDetailRespDto post = postService.공고디테일(id, userPS.getCInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 디테일 보기", post), HttpStatus.OK);
    }

    @PutMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<?> companyUpdatePost(@PathVariable int id,
            @RequestBody PostUpdateReqDto postUpdateReqDto) {
        User userPS = (User) session.getAttribute("principal");

        // 유효성 테스트

        PostUpdateDto post = postService.공고수정하기(postUpdateReqDto, id, userPS.getCInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 수정 성공", post), HttpStatus.CREATED);
    }

    @PostMapping("/posts")
    public ResponseEntity<?> postSave(PostSaveReqDto postSaveReqDto) {
        User userPS = (User) session.getAttribute("principal");
        if (userPS == null) {
            throw new CustomException("인증이 필요합니다");
        }

        PostSaveDto post = postService.공고등록(postSaveReqDto, userPS.getCInfoId());
        return new ResponseEntity<>(new ResponseDto<>(1, "공고 등록 완료", post), HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<?> companyDeletePost(@PathVariable int id) {
        User userPS = (User) session.getAttribute("principal");
        if (userPS == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED);
        }

        postService.공고삭제하기(id, userPS.getCInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 삭제 성공", null), HttpStatus.OK);
    }

}

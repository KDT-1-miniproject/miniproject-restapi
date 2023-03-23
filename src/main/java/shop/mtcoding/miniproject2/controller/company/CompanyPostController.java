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
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;
import shop.mtcoding.miniproject2.service.PostService;

@RequestMapping("/company")
@RequiredArgsConstructor
@RestController
public class CompanyPostController {

    private final PostService postService;
    private final HttpSession session;

    @GetMapping("/posts")
    public ResponseEntity<?> posts() {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        List<PostTitleRespDto> postTitleList = postService.기업공고리스트(principal.getCInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "기업 공고 리스트 보기", postTitleList), HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<?> postDetail(@PathVariable int id) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        CompanyPostDetailRespDto post = postService.기업공고디테일(id, principal.getCInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 디테일 보기", post), HttpStatus.OK);
    }

    @PutMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<?> companyUpdatePost(@PathVariable int id,
            @RequestBody PostUpdateReqDto postUpdateReqDto) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        // 유효성 테스트

        PostUpdateDto post = postService.공고수정하기(postUpdateReqDto, id, principal.getCInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 수정 성공", post), HttpStatus.CREATED);
    }

    @PostMapping("/posts")
    public ResponseEntity<?> postSave(PostSaveReqDto postSaveReqDto) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        PostSaveDto post = postService.공고등록(postSaveReqDto, principal.getCInfoId());
        return new ResponseEntity<>(new ResponseDto<>(1, "공고 등록 완료", post), HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<?> companyDeletePost(@PathVariable int id) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        postService.공고삭제하기(id, principal.getCInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 삭제 성공", null), HttpStatus.OK);
    }

}

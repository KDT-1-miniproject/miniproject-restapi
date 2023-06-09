package shop.mtcoding.miniproject2.controller.person;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeInsertReqDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeUpdateReqDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRes.ResumeDetailDto;
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.Resume;
import shop.mtcoding.miniproject2.model.ResumeRepository;
import shop.mtcoding.miniproject2.service.ResumeService;

@RequestMapping("/person")
@RequiredArgsConstructor
@RestController
public class PersonResumeController {
    private final ResumeRepository resumeRepository;
    private final ResumeService resumeService;
    private final HttpSession session;

    // saveResumeForm updateResume

    @GetMapping("/resumes")
    public ResponseEntity<?> resumes() {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        int pInfoId = principal.getPInfoId();
        List<Resume> resumeAll = resumeRepository.findAllByPInfoId(pInfoId);

        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 전체 불러오기 성공", resumeAll), HttpStatus.OK);
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<?> resumeDelete(@PathVariable int id) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        resumeService.delete(id, principal.getPInfoId());
        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 삭제 성공", null), HttpStatus.OK);
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<?> resumeDetail(@PathVariable int id) {

        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        ResumeDetailDto resumeDetailDto = resumeRepository.findDetailList(id);
        if (resumeDetailDto == null) {
            throw new CustomApiException("이력서가 존재하지 않습니다.");
        }
        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 디테일 보기", resumeDetailDto), HttpStatus.OK);
    }

    @PostMapping("/resumes")
    public ResponseEntity<?> resumeInsert(@Valid @RequestBody ResumeInsertReqDto resumeInsertReqDto,
            BindingResult bindingResult) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        // 유효성 테스트
        int pInfoId = principal.getPInfoId();
        Resume dto = resumeService.insertNewResume(pInfoId, resumeInsertReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 저장에 성공하였습니다!", dto), HttpStatus.OK);
    }

    @PutMapping("/resumes/{id}")
    public ResponseEntity<?> resumeUpdate(@PathVariable int id,
            @Valid @RequestBody ResumeUpdateReqDto resumeUpdateReqDto, BindingResult bindingResult) {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        int pInfoId = principal.getPInfoId();

        resumeService.updateById(id, pInfoId, resumeUpdateReqDto);
        Resume resumePS = resumeRepository.findById(id);

        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 수정에 성공하였습니다!", resumePS), HttpStatus.OK);
    }

}

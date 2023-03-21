package shop.mtcoding.miniproject2.controller.person;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeUpdateReqDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.Person;
import shop.mtcoding.miniproject2.model.PersonRepository;
import shop.mtcoding.miniproject2.model.Resume;
import shop.mtcoding.miniproject2.model.ResumeRepository;
import shop.mtcoding.miniproject2.model.Skill;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.service.ResumeService;

@RequestMapping("/person")
@RequiredArgsConstructor
@RestController
public class PersonResumeController {
    private final ResumeRepository resumeRepository;
    private final ResumeService resumeService;
    private final PersonRepository personRepository;
    private final SkillRepository skillRepository;
    private final HttpSession session;

    // saveResumeForm updateResume

    @GetMapping("/resumes")
    public ResponseEntity<?> resumes() {
        // 수정
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        int pInfoId = principal.getPInfoId();
        List<Resume> resumeAll = resumeRepository.findAllByPInfoId(pInfoId);
        // model.addAttribute("resumes", resumeAll);
        // model.addAttribute("count", resumeAll.size());
        Person personPS = personRepository.findById(pInfoId);
        // model.addAttribute("personPS", personPS);

        return new ResponseEntity<>(new ResponseDto<>(1, "", null), HttpStatus.OK);
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<?> resumeDelete(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        resumeService.delete(id);
        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 삭제 성공", null), HttpStatus.OK);
    }

    @GetMapping("/resume/{id}")
    public ResponseEntity<?> resumeDetail(@PathVariable int id) {

        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        Resume resumePS = resumeRepository.findById(id);
        if (resumePS == null) {
            throw new CustomException("없는 이력서를 수정할 수 없습니다");
        }
        Person personPS = personRepository.findById(resumePS.getPInfoId());

        Skill skillPS = skillRepository.findByResumeId(resumePS.getId());

        Date date = new Date(personPS.getBirthday().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedBirthday = sdf.format(date);
        // model.addAttribute("resumeDetail", resumePS);
        // model.addAttribute("personDetail", personPS);
        // model.addAttribute("birthday", formattedBirthday);
        // model.addAttribute("skillDetail", skillPS.getSkills()

        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 디테일 보기", null), HttpStatus.OK);
    }

    @PostMapping("/resume")
    public ResponseEntity<?> resumeInsert(ResumeUpdateReqDto resumeUpdateReqDto) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        // 유효성 테스트
        int pInfoId = principal.getPInfoId();
        resumeService.insertNewResume(pInfoId, resumeUpdateReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "", null), HttpStatus.OK);
    }

    @PutMapping("/resume/{id}")
    public ResponseEntity<?> resumeUpdate(@PathVariable int id, ResumeUpdateReqDto resumeUpdateReqDto) {
        User principal = (User) session.getAttribute("principal");
        // 유효성
        int pInfoId = principal.getPInfoId();
        resumeService.updateById(id, pInfoId, resumeUpdateReqDto);
        Resume resumePS = resumeRepository.findById(id);
        // model.addAttribute("resumePS", resumePS);

        return new ResponseEntity<>(new ResponseDto<>(1, "", null), HttpStatus.OK);
    }

}

package shop.mtcoding.miniproject2.controller.person;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.person.PersonReqDto.PersonUpdateDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.Person;
import shop.mtcoding.miniproject2.model.PersonRepository;
import shop.mtcoding.miniproject2.model.Skill;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.model.UserRepository;
import shop.mtcoding.miniproject2.service.PersonService;
import shop.mtcoding.miniproject2.util.EncryptionUtils;

@RequestMapping("/person")
@RequiredArgsConstructor
@RestController
public class PersonController {
    private final HttpSession session;
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @GetMapping("/info")
    public ResponseEntity<?> resumeDetail(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");

        Person PersonPS = personRepository.findById(principal.getPInfoId());

        Skill pSkill = skillRepository.findByPInfoId(principal.getPInfoId());
        // null point exception
        String pSkills = pSkill.getSkills();
        String[] pSkillArr = pSkills.split(",");

        // model.addAttribute("person", PersonPS);
        // model.addAttribute("pSkillArr", pSkillArr);
        return new ResponseEntity<>(new ResponseDto<>(1, "", null), HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateInfo(@RequestBody PersonUpdateDto personUpdateDto) {
        // 필수인지 헷갈림
        User principal = (User) session.getAttribute("principal");
        Person PersonPS = personRepository.findById(principal.getPInfoId());

        // 유효성 테스트

        String pw = EncryptionUtils.encrypt(personUpdateDto.getOriginPassword(), principal.getSalt());

        if (!pw.equals(principal.getPassword())) {
            throw new CustomApiException("비밀번호가 일치하지 않습니다!");
        }

        personService.update(personUpdateDto, principal.getPInfoId());
        User principalPS = (User) userRepository.findById(principal.getId());

        session.setAttribute("principal", principalPS);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원 정보 수정 완료", null), HttpStatus.OK);
    }
}

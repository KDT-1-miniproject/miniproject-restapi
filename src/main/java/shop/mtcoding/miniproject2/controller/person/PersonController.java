package shop.mtcoding.miniproject2.controller.person;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.person.PersonInfoInDto;
import shop.mtcoding.miniproject2.dto.person.PersonInfoOutDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.Person;
import shop.mtcoding.miniproject2.model.PersonRepository;
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
    public ResponseEntity<?> info() {
        User principal = (User) session.getAttribute("principal");
        PersonInfoOutDto pInfoDto = personRepository.findByIdWithSkills(principal.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "person info", pInfoDto), HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateInfo(@RequestBody PersonInfoInDto personInfoInDto) {

        User principal = (User) session.getAttribute("principal");
        Person PersonPS = personRepository.findById(principal.getPInfoId());
        if (PersonPS == null) {
            throw new CustomApiException("정보를 찾을 수 없습니다!");
        }

        personService.update(personInfoInDto);

        PersonInfoOutDto pInfoDto = personRepository.findByIdWithSkills(principal.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "회원 정보 수정 완료", pInfoDto), HttpStatus.OK);
    }
}

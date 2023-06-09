package shop.mtcoding.miniproject2.controller.person;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.person.PersonInfoInDto;
import shop.mtcoding.miniproject2.dto.person.PersonInfoOutDto;
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;
import shop.mtcoding.miniproject2.model.PersonRepository;
import shop.mtcoding.miniproject2.service.PersonService;

@RequestMapping("/person")
@RequiredArgsConstructor
@RestController
public class PersonController {
    private final HttpSession session;
    private final PersonService personService;
    private final PersonRepository personRepository;

    @GetMapping("/info")
    public ResponseEntity<?> info() {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");
        PersonInfoOutDto pInfoDto = personRepository.findByIdWithSkills(principal.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "person info", pInfoDto), HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateInfo(@Valid @RequestBody PersonInfoInDto personInfoInDto,
            BindingResult bindingResult) {

        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        personService.update(personInfoInDto);

        PersonInfoOutDto pInfoDto = personRepository.findByIdWithSkills(principal.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "회원 정보 수정 완료", pInfoDto), HttpStatus.OK);
    }
}

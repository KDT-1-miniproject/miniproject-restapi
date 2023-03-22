package shop.mtcoding.miniproject2.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.company.CompanyReq.JoinCompanyReqDto;
import shop.mtcoding.miniproject2.dto.company.CompanyReq.LoginCompanyReqDto;
import shop.mtcoding.miniproject2.dto.company.CompanyRespDto.JoinCompanyRespDto;
import shop.mtcoding.miniproject2.dto.person.PersonReq.JoinPersonReqDto;
import shop.mtcoding.miniproject2.dto.person.PersonReq.LoginPersonReqDto;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.service.CompanyService;
import shop.mtcoding.miniproject2.service.PersonService;

@RequiredArgsConstructor
@RestController
public class IndexController {

    private final HttpSession session;
    private final PersonService personService;
    private final CompanyService companyService;

    @GetMapping("/")
    public @ResponseBody ResponseEntity<?> main() {

        // jwt 해서 principal 이 존재하고 person이면 person 페이지
        // company면 company페이지를 넘겨주도록하자.

        return new ResponseEntity<>(new ResponseDto<>(1, null, null),
                HttpStatus.OK);
    }

    // GetMapping중 화면만 return 하는것은 제외
    // companyLoginForm, companyJoinForm
    // personLoginForm, personJoinForm1, personJoinForm2

    @PostMapping("/companyLogin")
    public @ResponseBody ResponseEntity<?> companyLogin(LoginCompanyReqDto loginCompanyReqDto) {

        User principal = companyService.기업로그인(loginCompanyReqDto);
        session.setAttribute("principal", principal);
        return new ResponseEntity<>(new ResponseDto<>(1, "로그인 완료", principal),
                HttpStatus.OK);
    }

    @PostMapping("/companyJoin")
    public @ResponseBody ResponseEntity<?> companyJoin(JoinCompanyReqDto joinCompanyReqDto) {
        JoinCompanyRespDto dto = companyService.기업회원가입(joinCompanyReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 완료", dto),
                HttpStatus.OK);
    }

    @PostMapping("/personLogin")
    public @ResponseBody ResponseEntity<?> personLogin(LoginPersonReqDto loginPersonReqDto) {

        User principal = personService.개인로그인(loginPersonReqDto);

        session.setAttribute("principal", principal);
        return new ResponseEntity<>(new ResponseDto<>(1, "로그인 완료", principal),
                HttpStatus.OK);
    }

    @PostMapping("/personJoin")
    public @ResponseBody ResponseEntity<?> personJoin(JoinPersonReqDto joinPersonReqDto,
            RedirectAttributes redirectAttributes) {

        return new ResponseEntity<>(new ResponseDto<>(1, "", null),
                HttpStatus.OK);
    }

    @PostMapping("/personJoin2")
    public @ResponseBody ResponseEntity<?> personJoin2(String[] skills, Integer pInfoId) {

        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 완료", null),
                HttpStatus.OK);
    }

    @GetMapping("/logout")
    public @ResponseBody ResponseEntity<?> logout() {
        session.invalidate();

        return new ResponseEntity<>(new ResponseDto<>(1, "로그아웃 완료", null),
                HttpStatus.OK);
    }

    @GetMapping("/customerService")
    public @ResponseBody ResponseEntity<?> customerService() {

        // 고객센터
        return new ResponseEntity<>(new ResponseDto<>(1, "", null),
                HttpStatus.OK);
    }
}

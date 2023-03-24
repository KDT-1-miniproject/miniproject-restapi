package shop.mtcoding.miniproject2.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.company.CompanyReq.JoinCompanyReqDto;
import shop.mtcoding.miniproject2.dto.company.CompanyReq.LoginCompanyReqDto;
import shop.mtcoding.miniproject2.dto.company.CompanyRespDto.JoinCompanyRespDto;
import shop.mtcoding.miniproject2.dto.customerService.CustomerServDto;
import shop.mtcoding.miniproject2.dto.person.PersonReq.JoinPersonReqDto;
import shop.mtcoding.miniproject2.dto.person.PersonReq.LoginPersonReqDto;
import shop.mtcoding.miniproject2.dto.person.PersonRespDto.JoinPersonRespDto;
import shop.mtcoding.miniproject2.service.CSService;
import shop.mtcoding.miniproject2.service.CompanyService;
import shop.mtcoding.miniproject2.service.PersonService;

@RequiredArgsConstructor
@RestController
public class IndexController {

    private final CSService csService;
    private final HttpSession session;
    private final PersonService personService;
    private final CompanyService companyService;

    @GetMapping("/")
    public @ResponseBody ResponseEntity<?> main() {

        return new ResponseEntity<>(new ResponseDto<>(1, null, null),
                HttpStatus.OK);
    }

    @PostMapping("/companyLogin")
    public @ResponseBody ResponseEntity<?> companyLogin(@Valid LoginCompanyReqDto loginCompanyReqDto,
            BindingResult bindingResult) {

        ResponseEntity<?> responseEntity = companyService.기업로그인(loginCompanyReqDto);
        return responseEntity;
    }

    @PostMapping("/companyJoin")
    public @ResponseBody ResponseEntity<?> companyJoin(@Valid @RequestBody JoinCompanyReqDto joinCompanyReqDto,
            BindingResult bindingResult) {

        JoinCompanyRespDto dto = companyService.기업회원가입(joinCompanyReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "기업 회원가입 완료", dto),
                HttpStatus.OK);
    }

    @PostMapping("/personLogin")
    public @ResponseBody ResponseEntity<?> personLogin(@Valid LoginPersonReqDto loginPersonReqDto,
            BindingResult bindingResult) {
        ResponseEntity<?> responseEntity = personService.개인로그인(loginPersonReqDto);
        return responseEntity;
    }

    @PostMapping("/personJoin")
    public @ResponseBody ResponseEntity<?> personJoin(@Valid @RequestBody JoinPersonReqDto joinPersonReqDto,
            BindingResult bindingResult) {

        JoinPersonRespDto dto = personService.개인회원가입(joinPersonReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 완료", dto),
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
        CustomerServDto scDto = csService.고객센터();
        return new ResponseEntity<>(new ResponseDto<>(1, "고객센터", scDto),
                HttpStatus.OK);
    }
}

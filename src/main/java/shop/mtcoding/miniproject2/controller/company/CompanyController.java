package shop.mtcoding.miniproject2.controller.company;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.company.CompanyReqDto.CompanyUpdateInfoInDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.Company;
import shop.mtcoding.miniproject2.model.CompanyRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.model.UserRepository;
import shop.mtcoding.miniproject2.service.CompanyService;

@RequestMapping("/company")
@RequiredArgsConstructor
@RestController
public class CompanyController {
    private final HttpSession session;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;

    // companyMain
    @GetMapping("/info")
    public ResponseEntity<?> info(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        Company companyPS = companyRepository.findById(principal.getCInfoId());
        // model.addAttribute("companyPS", companyPS);
        return new ResponseEntity<>(new ResponseDto<>(1, "company info", companyPS), HttpStatus.OK);
    }

    @PostMapping("/company/info")
    public ResponseEntity<?> companyUpdateInfo(@ModelAttribute CompanyUpdateInfoInDto companyUpdateInfoDto)
            throws IOException {

        User principal = (User) session.getAttribute("principal");
        // 유효성

        companyService.updateInfo(companyUpdateInfoDto);

        // post라서 data 필요 없는 거 맞겠지?!
        return new ResponseEntity<>(new ResponseDto<>(1, "기업 정보 수정 완료", null), HttpStatus.OK);
    }

}

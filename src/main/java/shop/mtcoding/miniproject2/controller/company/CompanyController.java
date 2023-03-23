package shop.mtcoding.miniproject2.controller.company;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.company.CompanyInfoInDto;
import shop.mtcoding.miniproject2.dto.company.CompanyInfoOutDto;
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
    public ResponseEntity<?> info() {
        User principal = (User) session.getAttribute("principal");
        CompanyInfoOutDto cInfoDto = companyRepository.findByIdWithUser(principal.getCInfoId());
        // model.addAttribute("companyPS", companyPS);
        return new ResponseEntity<>(new ResponseDto<>(1, "company info", cInfoDto), HttpStatus.OK);
    }

    @PutMapping("/info")
    public @ResponseBody ResponseEntity<?> companyUpdateInfo(@RequestBody CompanyInfoInDto companyInfoInDto)
            throws IOException {

        User principal = (User) session.getAttribute("principal");
        // 유효성

        companyService.updateInfo(companyInfoInDto);
        CompanyInfoOutDto cInfoDto = companyRepository.findByIdWithUser(principal.getCInfoId());
        // post라서 data 필요 없는 거 맞겠지?!
        return new ResponseEntity<>(new ResponseDto<>(1, "기업 정보 수정 완료", cInfoDto), HttpStatus.OK);
    }

}

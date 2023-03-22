package shop.mtcoding.miniproject2.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.miniproject2.dto.company.CompanyInfoInDto;
import shop.mtcoding.miniproject2.dto.company.CompanyReq.JoinCompanyReqDto;
import shop.mtcoding.miniproject2.dto.company.CompanyReq.LoginCompanyReqDto;
import shop.mtcoding.miniproject2.dto.company.CompanyRespDto.JoinCompanyRespDto;
import shop.mtcoding.miniproject2.dto.company.CompanyRespDto.JoinCompanyRespDto.UserDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.Company;
import shop.mtcoding.miniproject2.model.CompanyRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.model.UserRepository;
import shop.mtcoding.miniproject2.util.EncryptionUtils;
import shop.mtcoding.miniproject2.util.PathUtil;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Transactional
    public JoinCompanyRespDto 기업회원가입(JoinCompanyReqDto joinCompanyReqDto) {

        Company sameCompany = companyRepository.findByCompanyNameAndNumber(joinCompanyReqDto.getName(),
                joinCompanyReqDto.getNumber());

        if (sameCompany != null) {
            throw new CustomException("이미 가입되어 있는 기업입니다.");
        }
        Company company = new Company();
        company.setName(joinCompanyReqDto.getName());
        company.setNumber(joinCompanyReqDto.getNumber());
        company.setAddress(joinCompanyReqDto.getAddress());
        company.setManagerName(joinCompanyReqDto.getManagerName());

        int result = companyRepository.insert(company);

        if (result != 1) {
            throw new CustomException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String salt = EncryptionUtils.getSalt();
        joinCompanyReqDto
                .setPassword(EncryptionUtils.encrypt(joinCompanyReqDto.getPassword(), salt));

        User user = new User(joinCompanyReqDto.getEmail(), joinCompanyReqDto.getPassword(), salt, 0, company.getId());

        int result2 = userRepository.insert(user);
        if (result2 != 1) {
            throw new CustomException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User userPS = userRepository.findById(user.getId());
        UserDto userdto = new UserDto(userPS.getId(), userPS.getEmail(), userPS.getCreatedAt());
        JoinCompanyRespDto dto = new JoinCompanyRespDto(company, userdto);

        return dto;
    }

    @Transactional
    public void updateInfo(CompanyInfoInDto companyInfoInDto) {

        User principal = (User) session.getAttribute("principal");
        Company companyPS = companyRepository.findById(principal.getCInfoId());
        User userPS = userRepository.findById(principal.getId());

        String password;

        if (companyInfoInDto.getPassword() == null || companyInfoInDto.getPassword().isEmpty()) {
            password = userPS.getPassword();
        } else {
            password = EncryptionUtils.encrypt(companyInfoInDto.getPassword(), principal.getSalt());
        }

        if (companyInfoInDto.getLogo() == null || companyInfoInDto.getLogo().isEmpty()) {
            if (companyPS.getLogo() == null || companyPS.getLogo().isEmpty()) {
                companyPS.setLogo("");
            } else {
                companyPS.setLogo(companyPS.getLogo());
            }
        } else {
            String uuidComapnyLogo = PathUtil.writeImageFile(companyInfoInDto.getLogo());
            companyPS.setLogo(uuidComapnyLogo);
        }

        String t = companyInfoInDto.getCyear();
        String[] times = t.split("-");
        int cyear = Integer.parseInt(times[0]);

        int result = companyRepository.updateById(principal.getCInfoId(), companyPS.getLogo(), companyPS.getName(),
                companyPS.getNumber(), companyInfoInDto.getBossName(), companyInfoInDto.getAddress(),
                companyInfoInDto.getManagerName(),
                companyInfoInDto.getManagerPhone(), companyInfoInDto.getSize(), cyear,
                companyPS.getCreatedAt());

        int result2 = userRepository.updateById(principal.getId(), principal.getEmail(), password,
                principal.getPInfoId(), principal.getCInfoId(), userPS.getCreatedAt());

        if (result != 1) {
            throw new CustomApiException("기업 정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (result2 != 1) {
            throw new CustomApiException("기업 정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public User 기업로그인(LoginCompanyReqDto loginCompanyReqDto) {
        User userCheck = userRepository.findByEmail(loginCompanyReqDto.getEmail());
        if (userCheck == null) {
            throw new CustomApiException("이메일 혹은 패스워드가 잘못입력되었습니다1.");
        }
        // DB Salt 값
        String salt = userCheck.getSalt();
        // DB Salt + 입력된 password 해싱
        loginCompanyReqDto.setPassword(EncryptionUtils.encrypt(loginCompanyReqDto.getPassword(), salt));
        User principal = userRepository.findCompanyByEmailAndPassword(loginCompanyReqDto.getEmail(),
                loginCompanyReqDto.getPassword());
        if (principal == null) {
            throw new CustomApiException("이메일 혹은 패스워드가 잘못입력되었습니다2.");
        }

        return principal;
    }

}

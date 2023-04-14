package shop.mtcoding.miniproject2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import shop.mtcoding.miniproject2.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendScrapDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeWithPostInfoRecommendDto;
import shop.mtcoding.miniproject2.dto.company.CompanyInfoInDto;
import shop.mtcoding.miniproject2.dto.company.CompanyReq.JoinCompanyReqDto;
import shop.mtcoding.miniproject2.dto.company.CompanyReq.LoginCompanyReqDto;
import shop.mtcoding.miniproject2.dto.company.CompanyRespDto.JoinCompanyRespDto;
import shop.mtcoding.miniproject2.dto.company.CompanyRespDto.JoinCompanyRespDto.UserDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.postIdAndSkillsDto;
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.Company;
import shop.mtcoding.miniproject2.model.CompanyRepository;
import shop.mtcoding.miniproject2.model.CompanyScrap;
import shop.mtcoding.miniproject2.model.CompanyScrapRepository;
import shop.mtcoding.miniproject2.model.PostRepository;
import shop.mtcoding.miniproject2.model.ResumeRepository;
import shop.mtcoding.miniproject2.model.SkillFilter;
import shop.mtcoding.miniproject2.model.SkillFilterRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.model.UserRepository;
import shop.mtcoding.miniproject2.util.EncryptionUtils;
import shop.mtcoding.miniproject2.util.JwtProvider;
import shop.mtcoding.miniproject2.util.PathUtil;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final HttpSession session;
    private final ResumeRepository resumeRepository;
    private final PostRepository postRepository;
    private final SkillFilterRepository skillFilterRepository;
    private final CompanyScrapRepository companyScrapRepository;

    @Transactional
    public JoinCompanyRespDto 기업회원가입(@RequestBody JoinCompanyReqDto joinCompanyReqDto) {

        Company sameCompany = companyRepository.findByCompanyNameAndNumber(joinCompanyReqDto.getName(),
                joinCompanyReqDto.getNumber());

        if (sameCompany != null) {
            throw new CustomApiException("이미 가입되어 있는 기업입니다.");
        }
        Company company = new Company();
        company.setName(joinCompanyReqDto.getName());
        company.setNumber(joinCompanyReqDto.getNumber());
        company.setAddress(joinCompanyReqDto.getAddress());
        company.setManagerName(joinCompanyReqDto.getManagerName());

        int result = companyRepository.insert(company);

        if (result != 1) {
            throw new CustomApiException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String salt = EncryptionUtils.getSalt();
        joinCompanyReqDto
                .setPassword(EncryptionUtils.encrypt(joinCompanyReqDto.getPassword(), salt));

        User user = new User(joinCompanyReqDto.getEmail(), joinCompanyReqDto.getPassword(), salt, 0, company.getId());

        int result2 = userRepository.insert(user);
        if (result2 != 1) {
            throw new CustomApiException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // User userPS = userRepository.findById(user.getId());
        // UserDto userdto = new UserDto(userPS.getId(), userPS.getEmail(),
        // userPS.getCreatedAt());
        // JoinCompanyRespDto dto = new JoinCompanyRespDto(company, userdto);
        JoinCompanyRespDto dto = companyRepository.findByIdWithUserJoin(user.getCInfoId());
        return dto;
    }

    @Transactional
    public void updateInfo(CompanyInfoInDto companyInfoInDto) {

        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");
        Company companyPS = companyRepository.findById(principal.getCInfoId());
        User userPS = userRepository.findById(principal.getId());

        if (companyPS == null) {
            throw new CustomApiException("정보를 찾을 수 없습니다!");
        }

        String pw = EncryptionUtils.encrypt(companyInfoInDto.getOriginPassword(), userPS.getSalt());
        if (!pw.equals(userPS.getPassword())) {
            throw new CustomApiException("비밀번호가 일치하지 않습니다!");
        }

        String password;
        if (companyInfoInDto.getPassword() == null || companyInfoInDto.getPassword().isEmpty()) {
            password = userPS.getPassword();
        } else {
            password = EncryptionUtils.encrypt(companyInfoInDto.getPassword(), userPS.getSalt());
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
                companyInfoInDto.getManagerPhone(), Integer.parseInt(companyInfoInDto.getSize()), cyear,
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

    @Transactional(readOnly = true)
    public ResponseEntity<?> 기업로그인(LoginCompanyReqDto loginCompanyReqDto) {
        User userCheck = userRepository.findByEmail(loginCompanyReqDto.getEmail());
        if (userCheck == null) {
            throw new CustomApiException("이메일 혹은 패스워드가 잘못입력되었습니다.");
        }
        // DB Salt 값
        String salt = userCheck.getSalt();
        // DB Salt + 입력된 password 해싱
        loginCompanyReqDto.setPassword(EncryptionUtils.encrypt(loginCompanyReqDto.getPassword(), salt));
        User principal = userRepository.findCompanyByEmailAndPassword(loginCompanyReqDto.getEmail(),
                loginCompanyReqDto.getPassword());
        if (principal == null) {
            throw new CustomApiException("이메일 혹은 패스워드가 잘못입력되었습니다.");
        }

        String jwt = JwtProvider.create(principal);

        // header에 담기
        ResponseEntity<Object> response = new ResponseEntity<>(new ResponseDto<>(1, "로그인 완료", principal),
                HttpStatus.OK);

        HttpHeaders headers = new HttpHeaders();
        headers.putAll(response.getHeaders());
        headers.add(JwtProvider.HEADER, jwt);

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(response.getBody(), headers,
                response.getStatusCode());

        return responseEntity;
    }

    @Transactional(readOnly = true)
    public List<ResumeWithPostInfoRecommendDto> recommend() {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        List<postIdAndSkillsDto> postAndSkillsList = postRepository.findPostIdAndSkills(principal.getCInfoId());

        List<ResumeWithPostInfoRecommendDto> resumeAndPostInfo = new ArrayList<>();

        for (postIdAndSkillsDto p : postAndSkillsList) {
            String[] skills = p.getSkills().split(",");
            List<SkillFilter> sFilters = new ArrayList<>();
            for (String skill : skills) {
                List<SkillFilter> s = skillFilterRepository.findSkillNameForCompany(skill);
                sFilters.addAll(s);
            }

            // resume id로 count
            HashMap<Integer, Integer> resumeIdAndCount = new HashMap<>();
            for (SkillFilter sf : sFilters) {

                resumeIdAndCount.put(sf.getResumeId(), resumeIdAndCount.getOrDefault(sf.getResumeId(), 0) + 1);

            }

            Set<Integer> key = resumeIdAndCount.keySet();
            HashMap<Integer, Integer> resumeIdAndCount2 = new HashMap<>();
            for (Integer k : key) {
                Integer count = resumeIdAndCount.getOrDefault(k, 0);

                if (count >= 2) {
                    resumeIdAndCount2.put(k, count);
                }

            }

            // 내림차순 정렬
            List<Entry<Integer, Integer>> resumeIdList = new ArrayList<>(resumeIdAndCount2.entrySet());
            Collections.sort(resumeIdList, new Comparator<Entry<Integer, Integer>>() {
                public int compare(Entry<Integer, Integer> c1, Entry<Integer, Integer> c2) {
                    return c2.getValue().compareTo(c1.getValue());
                }
            });

            // RESUME LIST
            List<ResumeRecommendScrapDto> resumeList = new ArrayList<>();
            for (Entry<Integer, Integer> entry : resumeIdList) {
                ResumeRecommendDto resumePS = resumeRepository.findNameAndTitleAndSkills(entry.getKey());

                ResumeRecommendScrapDto resumeScrapDto = new ResumeRecommendScrapDto(resumePS);

                CompanyScrap cs = companyScrapRepository.findByCInfoIdAndResumeId(principal.getCInfoId(),
                        resumePS.getId());

                if (cs == null) {
                    resumeScrapDto.setScrap(0);
                } else {
                    resumeScrapDto.setScrap(1);
                }

                resumeList.add(resumeScrapDto);

            }

            String title = postRepository.findById(p.getPostId()).getTitle();

            ResumeWithPostInfoRecommendDto resumeAndPost = new ResumeWithPostInfoRecommendDto(p.getPostId(), title,
                    resumeList);

            resumeAndPostInfo.add(resumeAndPost);
        }

        return resumeAndPostInfo;

    }
}

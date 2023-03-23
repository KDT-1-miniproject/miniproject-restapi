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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendScrapDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeWithPostInfoRecommendDto;
import shop.mtcoding.miniproject2.dto.company.CompanyInfoInDto;
import shop.mtcoding.miniproject2.dto.company.CompanyReq.JoinCompanyReqDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.postIdAndSkillsDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
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
import shop.mtcoding.miniproject2.util.PathUtil;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SkillFilterRepository skillFilterRepository;

    @Autowired
    private CompanyScrapRepository companyScrapRepository;

    @Transactional
    public void join(JoinCompanyReqDto joinCompanyReqDto) {

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
        int result2 = userRepository.insert(joinCompanyReqDto.getEmail(), joinCompanyReqDto.getPassword(), salt, 0,
                company.getId());
        if (result2 != 1) {
            throw new CustomException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @Transactional(readOnly = true)
    public List<ResumeWithPostInfoRecommendDto> recommend() {
        User principal = (User) session.getAttribute("principal");

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

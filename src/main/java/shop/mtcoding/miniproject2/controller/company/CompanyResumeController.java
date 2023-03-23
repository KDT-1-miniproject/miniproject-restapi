package shop.mtcoding.miniproject2.controller.company;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendScrapDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeWithPostInfoRecommendDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.CompanyProposalListDateRespDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.CompanyProposalListRespDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.PersonProposalDetailRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.postIdAndSkillsDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.Company;
import shop.mtcoding.miniproject2.model.CompanyRepository;
import shop.mtcoding.miniproject2.model.CompanyScrap;
import shop.mtcoding.miniproject2.model.CompanyScrapRepository;
import shop.mtcoding.miniproject2.model.Person;
import shop.mtcoding.miniproject2.model.PersonProposalRepository;
import shop.mtcoding.miniproject2.model.PersonRepository;
import shop.mtcoding.miniproject2.model.PostRepository;
import shop.mtcoding.miniproject2.model.Resume;
import shop.mtcoding.miniproject2.model.ResumeRepository;
import shop.mtcoding.miniproject2.model.Skill;
import shop.mtcoding.miniproject2.model.SkillFilter;
import shop.mtcoding.miniproject2.model.SkillFilterRepository;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.model.UserRepository;
import shop.mtcoding.miniproject2.service.CompanyService;

@RequestMapping("/company")
@RequiredArgsConstructor
@RestController
public class CompanyResumeController {
    private final HttpSession session;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;
    private final PersonProposalRepository personProposalRepository;
    private final ResumeRepository resumeRepository;
    private final PersonRepository personRepository;
    private final CompanyScrapRepository companyScrapRepository;
    private final SkillFilterRepository skillFilterRepository;
    private final CompanyService companyService;

    @GetMapping("/resumes")
    public ResponseEntity<?> resume() {
        User userPS = (User) session.getAttribute("principal");
        List<CompanyProposalListRespDto> companyProposalList = personProposalRepository
                .findAllWithPostAndResumeAndPInfoByCInfoId(userPS.getCInfoId());

        List<CompanyProposalListDateRespDto> companyProposalList2 = new ArrayList<>();

        for (CompanyProposalListRespDto cpl : companyProposalList) {
            CompanyProposalListDateRespDto dto = new CompanyProposalListDateRespDto();
            dto.setCInfoId(cpl.getCInfoId());
            dto.setId(cpl.getId());
            dto.setName(cpl.getName());
            dto.setPInfoId(cpl.getPInfoId());
            dto.setPostId(cpl.getPostId());
            dto.setPtitle(cpl.getPtitle());
            dto.setRtitle(cpl.getRtitle());
            dto.setResumeId(cpl.getResumeId());
            dto.setStatus(cpl.getStatus());

            Timestamp createdAt = cpl.getCreatedAt();
            Date date = new Date(createdAt.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String proposaltime = sdf.format(date);

            dto.setCreatedAt(proposaltime);

            companyProposalList2.add(dto);
        }

        Company company = companyRepository.findById(userPS.getCInfoId());
        // model.addAttribute("companyPS", company);
        // model.addAttribute("companyProposalList", companyProposalList2);

        return new ResponseEntity<>(new ResponseDto<>(1, "", null), HttpStatus.OK);
    }

    @GetMapping("/resume/{id}")
    public ResponseEntity<?> resumeDetail(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        Resume resumePS = resumeRepository.findById(id);
        if (resumePS == null) {
            throw new CustomApiException("없는 이력서엔 접근할 수 없습니다.");
        }
        // 기업의 공고에 지원이력이 있는 이력서인지 확인
        // 기업의 공고에 없는 이력서라면 제안하기 버튼을 아니라면 합격 불합격 버튼을 두자
        List<PersonProposalDetailRespDto> proposalList = personProposalRepository
                .findAllWithPostByCInfoIdAndResumeId(principal.getCInfoId(), id);
        if (proposalList.size() > 0) {
            // 해당 이력서로 같은회사 다른 공고에 지원했을 수도 있음.
            // proposalList.get(0).getPostId(); //postId를 이용해서 어케 해보자...

            // model.addAttribute("proposal", proposalList);
        }
        Person personPS = personRepository.findById(resumePS.getPInfoId());
        User user = userRepository.findByPersonId(personPS.getId());
        Skill skillPS = skillRepository.findByPInfoId(resumePS.getPInfoId());
        // model.addAttribute("resumeDetail", resumePS);
        // model.addAttribute("personDetail", personPS);
        // model.addAttribute("personUser", user);
        // model.addAttribute("skillDetail", skillPS.getSkills().split(","));

        return new ResponseEntity<>(new ResponseDto<>(1, "", null), HttpStatus.OK);
    }

    // 공고 + 스킬 찾기
    @GetMapping("/recommend")
    public ResponseEntity<?> recommend() {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        // 공고 + 스킬 찾기

        List<ResumeWithPostInfoRecommendDto> postWithReumseDto = companyService.recommend();
        return new ResponseEntity<>(new ResponseDto<>(1, "기업 인재 추천", postWithReumseDto), HttpStatus.OK);
    }

}

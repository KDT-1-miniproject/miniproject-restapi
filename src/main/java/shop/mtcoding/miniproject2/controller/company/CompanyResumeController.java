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
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendArrDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeWithPostInfoRecommendDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.CompanyProposalListDateRespDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.CompanyProposalListRespDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.PersonProposalDetailRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.postIdAndSkillsDto;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
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
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        Resume resumePS = resumeRepository.findById(id);
        if (resumePS == null) {
            throw new CustomException("없는 이력서엔 접근할 수 없습니다.");
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

    @GetMapping("/recommend")
    public ResponseEntity<?> recommend() {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        // 공고 + 스킬 찾기
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
            List<ResumeRecommendArrDto> resumeList = new ArrayList<>();
            for (Entry<Integer, Integer> entry : resumeIdList) {
                ResumeRecommendDto resumePS = resumeRepository.findNameAndTitleAndSkills(entry.getKey());

                // System.out.println("테스트 :" + resumePS);

                ResumeRecommendArrDto dto = new ResumeRecommendArrDto(resumePS);

                // System.out.println("테스트 :" + dto);

                CompanyScrap cs = companyScrapRepository.findByCInfoIdAndResumeId(principal.getCInfoId(),
                        dto.getId());

                if (cs == null) {
                    dto.setScrap(0);
                } else {
                    dto.setScrap(1);
                }

                // System.out.println("테스트 :" + dto);

                resumeList.add(dto);
            }

            String title = postRepository.findById(p.getPostId()).getTitle();

            ResumeWithPostInfoRecommendDto resumeAndPost = new ResumeWithPostInfoRecommendDto(
                    p.getPostId(), title, resumeList);

            resumeAndPostInfo.add(resumeAndPost);
        }

        return new ResponseEntity<>(new ResponseDto<>(1, "", resumeAndPostInfo), HttpStatus.OK);
    }

}

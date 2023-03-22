package shop.mtcoding.miniproject2.controller.person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.post.PostRecommendOutDto.PostRecommendIntegerRespDto;
import shop.mtcoding.miniproject2.dto.post.PostRecommendOutDto.PostRecommendTimeStampResDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostDtailResDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostMainRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostMainWithScrapRespDto;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.Company;
import shop.mtcoding.miniproject2.model.CompanyRepository;
import shop.mtcoding.miniproject2.model.PersonScrap;
import shop.mtcoding.miniproject2.model.PersonScrapRepository;
import shop.mtcoding.miniproject2.model.Post;
import shop.mtcoding.miniproject2.model.PostRepository;
import shop.mtcoding.miniproject2.model.Resume;
import shop.mtcoding.miniproject2.model.ResumeRepository;
import shop.mtcoding.miniproject2.model.Skill;
import shop.mtcoding.miniproject2.model.SkillFilter;
import shop.mtcoding.miniproject2.model.SkillFilterRepository;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.util.CvTimestamp;

@RequestMapping("/person")
@RequiredArgsConstructor
@RestController
public class PersonPostController {
    private final HttpSession session;
    private final ResumeRepository resumeRepository;
    private final PostRepository postRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;
    private final SkillFilterRepository skillFilterRepository;
    private final PersonScrapRepository personScrapRepository;

    @GetMapping({ "/main", "/" })
    public ResponseEntity<?> main() {
        User principal = (User) session.getAttribute("principal");

        // 회사로고, 회사이름, 공고이름, 회사 주소, D-day
        // cInfo : 회사로고, 회사이름, 회사주소
        // 공고 정보 : 공고이름, 디데이
        List<PostMainRespDto> postList = (List<PostMainRespDto>) postRepository.findAllWithCInfo();
        List<PostMainWithScrapRespDto> postList2 = new ArrayList<>();
        for (PostMainRespDto p : postList) {
            PostMainWithScrapRespDto psDto = new PostMainWithScrapRespDto();

            try {
                PersonScrap ps = personScrapRepository.findByPInfoIdAndPostId(principal.getPInfoId(), p.getPostId());
                if (ps == null) {
                    psDto.setScrap(0);
                } else {
                    psDto.setScrap(1);
                }
                psDto.setAddress(p.getAddress());
                psDto.setCInfoId(p.getCInfoId());
                psDto.setDeadline(p.getDeadline());
                psDto.setLogo(p.getLogo());
                psDto.setName(p.getName());
                psDto.setPostId(p.getPostId());
                psDto.setTitle(p.getTitle());
                postList2.add(psDto);
            } catch (Exception e) {

            }
        }
        // 위 내용을 서비스로

        // model.addAttribute("mainPosts", postList2);
        // model.addAttribute("size", postList2.size());

        return new ResponseEntity<>(new ResponseDto<>(1, "", null), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable int id) {

        User userPS = (User) session.getAttribute("principal");
        if (userPS == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.FORBIDDEN);
        }

        Post postPS = (Post) postRepository.findById(id);
        if (postPS == null) {
            throw new CustomException("없는 공고 입니다.");
        }

        PersonScrap scrap = personScrapRepository.findByPInfoIdAndPostId(userPS.getPInfoId(), postPS.getId());

        PostDtailResDto postPS2 = new PostDtailResDto();
        postPS2.setId(postPS.getId());
        postPS2.setCInfoId(postPS.getCInfoId());
        postPS2.setCIntro(postPS.getCondition());
        postPS2.setCareer(postPS.getCareer());
        postPS2.setCondition(postPS.getCondition());
        postPS2.setEndHour(postPS.getEndHour());
        postPS2.setJobIntro(postPS.getJobIntro());
        postPS2.setPay(postPS.getPay());
        postPS2.setStartHour(postPS.getStartHour());
        postPS2.setTitle(postPS.getTitle());

        if (scrap == null) {
            postPS2.setScrap(0);
        } else {
            postPS2.setScrap(1);
        }

        Company companyPS = (Company) companyRepository.findById(postPS.getCInfoId());
        Skill skillPS = (Skill) skillRepository.findByPostId(id);
        StringTokenizer skills = new StringTokenizer(skillPS.getSkills(), ",");
        Date date = new Date(postPS.getDeadline().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDeadline = sdf.format(date);

        // model.addAttribute("post", postPS2);
        // model.addAttribute("company", companyPS);
        // model.addAttribute("deadline", formattedDeadline);
        // model.addAttribute("skills", skills);

        List<Resume> resumeList = (List<Resume>) resumeRepository.findAllByPInfoId(userPS.getPInfoId());
        // model.addAttribute("resume", resumeList);
        return new ResponseEntity<>(new ResponseDto<>(1, "", null), HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public ResponseEntity<?> recommend() {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.FORBIDDEN);
        }
        // person skill 찾기
        Skill principalSkills = skillRepository.findByPInfoId(principal.getPInfoId());

        String[] principalSKillArr = principalSkills.getSkills().split(",");

        List<SkillFilter> principalSkilFilters = new ArrayList<>();

        for (String principalSkill : principalSKillArr) {
            List<SkillFilter> s = skillFilterRepository.findSkillNameForPerson(principalSkill);
            principalSkilFilters.addAll(s);

        }

        // key : count 중복 포함하지 않고 map 저장
        HashMap<Integer, Integer> postAndCount = new HashMap<>();
        for (SkillFilter psf : principalSkilFilters) {
            postAndCount.put(psf.getPostId(), postAndCount.getOrDefault(psf.getPostId(), 0) + 1);
        }
        Set<Integer> key = postAndCount.keySet();

        HashMap<Integer, Integer> postAndCount2 = new HashMap<>();
        for (Integer k : key) {
            Integer count = postAndCount.getOrDefault(k, 0);
            // System.out.println("테스트: " + k + "-" + count);
            if (count >= 2) {
                postAndCount2.put(k, count);
            }
        }

        // 내림차순 정렬
        List<Entry<Integer, Integer>> postIdList = new ArrayList<>(postAndCount2.entrySet());
        Collections.sort(postIdList, new Comparator<Entry<Integer, Integer>>() {
            public int compare(Entry<Integer, Integer> c1, Entry<Integer, Integer> c2) {
                return c2.getValue().compareTo(c1.getValue());
            }
        });

        List<PostRecommendIntegerRespDto> postList = new ArrayList<>();
        for (Entry<Integer, Integer> entry : postIdList) {
            try {
                // System.out.println("테스트: 1");
                PostRecommendTimeStampResDto p = postRepository.findByPostIdToRecmmend(entry.getKey());
                System.out.println("테스트: " + entry.getKey());
                if (p == null) {
                    continue;
                }
                PostRecommendIntegerRespDto p2 = new PostRecommendIntegerRespDto(p);
                p2.setDeadline(CvTimestamp.ChangeDDay(p.getDeadline()));

                PersonScrap ps = personScrapRepository.findByPInfoIdAndPostId(principal.getPInfoId(), p2.getPostId());

                if (ps == null) {
                    p2.setScrap(0);
                } else {
                    p2.setScrap(1);
                }

                // System.out.println("테스트 : " + p2);

                postList.add(p2);
            } catch (Exception e) {
                throw new CustomException("실패");
            }
        }
        // model.addAttribute("postList", postList);

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 추천", postList), HttpStatus.OK);
    }
}

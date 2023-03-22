package shop.mtcoding.miniproject2.controller.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import shop.mtcoding.miniproject2.dto.post.PostResp.PersonPostDetailResDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostMainRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostRecommendIntegerRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostRecommendTimeStampResDto;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.dto.post.PostRecommendOutDto.PostRecommendIntegerRespDto;
import shop.mtcoding.miniproject2.dto.post.PostRecommendOutDto.PostRecommendTimeStampResDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostDtailResDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostMainRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostMainWithScrapRespDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.Company;

import shop.mtcoding.miniproject2.model.CompanyRepository;
import shop.mtcoding.miniproject2.model.PersonScrap;
import shop.mtcoding.miniproject2.model.PersonScrapRepository;
import shop.mtcoding.miniproject2.model.PostRepository;
import shop.mtcoding.miniproject2.model.ResumeRepository;
import shop.mtcoding.miniproject2.model.Skill;
import shop.mtcoding.miniproject2.model.SkillFilter;
import shop.mtcoding.miniproject2.model.SkillFilterRepository;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.service.PostService;
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
    private final PostService postService;

    @GetMapping({ "/main", "/" })
    public ResponseEntity<?> main() {
        User principal = (User) session.getAttribute("principal");

        // 회사로고, 회사이름, 공고이름, 회사 주소, D-day
        // cInfo : 회사로고, 회사이름, 회사주소
        // 공고 정보 : 공고이름, 디데이
        List<PostMainRespDto> posts = postService.개인공고리스트(principal.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "개인 공고 리스트 보기 ", posts), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable int id) {

        User userPS = (User) session.getAttribute("principal");
        if (userPS == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.FORBIDDEN);
        }

        PersonPostDetailResDto post = postService.개인공고디테일(id, userPS.getPInfoId());

        return new ResponseEntity<>(new ResponseDto<>(1, "개인 공고 디테일 보기", post), HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public ResponseEntity<?> recommend() {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.FORBIDDEN);
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
                // System.out.println("테스트: " + entry.getKey());
                if (p == null) {
                    continue;
                }
                PostRecommendIntegerRespDto p2 = new PostRecommendIntegerRespDto(p);
                p2.setDeadline(CvTimestamp.ChangeDDay(p.getDeadline()));

                PersonScrap ps = personScrapRepository.findByPInfoIdAndPostId(principal.getPInfoId(), p2.getId());

                if (ps == null) {
                    p2.setScrap(0);
                } else {
                    p2.setScrap(1);
                }

                // System.out.println("테스트 : " + p2);

                postList.add(p2);
            } catch (Exception e) {
                throw new CustomApiException("실패");
            }
        }
        // model.addAttribute("postList", postList);

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 추천", postList), HttpStatus.OK);
    }
}

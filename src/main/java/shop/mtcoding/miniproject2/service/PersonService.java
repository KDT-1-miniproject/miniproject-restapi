package shop.mtcoding.miniproject2.service;

import java.sql.Timestamp;
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

import shop.mtcoding.miniproject2.dto.person.PersonInfoInDto;
import shop.mtcoding.miniproject2.dto.person.PersonReq.JoinPersonReqDto;
import shop.mtcoding.miniproject2.dto.post.PostRecommendOutDto.PostRecommendIntegerRespDto;
import shop.mtcoding.miniproject2.dto.post.PostRecommendOutDto.PostRecommendTimeStampResDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.Person;
import shop.mtcoding.miniproject2.model.PersonRepository;
import shop.mtcoding.miniproject2.model.PersonScrap;
import shop.mtcoding.miniproject2.model.PersonScrapRepository;
import shop.mtcoding.miniproject2.model.PostRepository;
import shop.mtcoding.miniproject2.model.Skill;
import shop.mtcoding.miniproject2.model.SkillFilter;
import shop.mtcoding.miniproject2.model.SkillFilterRepository;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.model.UserRepository;
import shop.mtcoding.miniproject2.util.CvTimestamp;
import shop.mtcoding.miniproject2.util.EncryptionUtils;

@Service
public class PersonService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired

    private HttpSession session;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SkillFilterRepository skillFilterRepository;

    @Autowired
    private PersonScrapRepository personScrapRepository;

    @Transactional
    public int join(JoinPersonReqDto joinPersonReqDto) {
        // System.out.println(salt);
        Person samePerson = personRepository.findByPersonNameAndEmail(joinPersonReqDto.getName(),
                joinPersonReqDto.getEmail());
        if (samePerson != null) {
            throw new CustomException("이미 가입되어 있는 회원입니다.");
        }

        Person person = new Person();
        person.setName(joinPersonReqDto.getName());
        int result = personRepository.insert(person); // joinReqDto(인수)를 매핑
        if (result != 1) {
            throw new CustomException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Hash + Salt 다이제스트
        String salt = EncryptionUtils.getSalt();
        joinPersonReqDto
                .setPassword(EncryptionUtils.encrypt(joinPersonReqDto.getPassword(), salt));
        int result2 = userRepository.insert(joinPersonReqDto.getEmail(),
                joinPersonReqDto.getPassword(), salt, person.getId(),
                0);
        if (result2 != 1) {
            throw new CustomException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return person.getId();
    }

    @Transactional
    public void join2(String skills, int pInfoId) {
        int result = skillRepository.insert(pInfoId, 0, 0, skills);
        if (result != 1) {
            throw new CustomException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // public User 로그인(LoginReqPersonDto loginReqPersonDto) {
    // User principal =
    // personRepository.findByEmailAndPassword(loginReqPersonDto.getEmail(),
    // loginReqPersonDto.getPassword());

    // if (principal == null) {
    // throw new CustomException("이메일 혹은 패스워드가 잘못입력되었습니다.");
    // }
    // return principal;
    // }

    @Transactional
    public void update(PersonInfoInDto personInfoInDto, int pInfoId) {

        User principal = (User) session.getAttribute("principal");
        Person personPS = personRepository.findById(pInfoId);
        String password;

        if (personInfoInDto.getPassword() == null || personInfoInDto.getPassword().isEmpty()) {
            password = principal.getPassword();
        } else {
            password = EncryptionUtils.encrypt(personInfoInDto.getPassword(), principal.getSalt());
        }

        Timestamp birthday = Timestamp.valueOf(personInfoInDto.getBirthday());
        int result = personRepository.updateById(pInfoId, personInfoInDto.getName(), personInfoInDto.getPhone(),
                personInfoInDto.getAddress(), birthday);

        if (result != 1) {
            throw new CustomApiException("정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Skill skillPS = skillRepository.findByPInfoId(pInfoId);

        if (skillPS == null) {
            throw new CustomApiException("정보를 찾을 수 없습니다");
        }

        int result2 = skillRepository.updateById(skillPS.getId(), pInfoId, 0, 0, personInfoInDto.getSkills(),
                skillPS.getCreatedAt());

        if (result2 != 1) {
            throw new CustomApiException("정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int result3 = userRepository.updateById(principal.getId(), principal.getEmail(), password,
                principal.getPInfoId(),
                principal.getCInfoId(), personPS.getCreatedAt());

        if (result3 != 1) {
            throw new CustomApiException("정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<PostRecommendIntegerRespDto> recommend() {
        User principal = (User) session.getAttribute("principal");
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

        return postList;
    }

}

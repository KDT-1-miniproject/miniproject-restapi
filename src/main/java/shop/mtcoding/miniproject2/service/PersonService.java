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

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.ResponseDto;
import shop.mtcoding.miniproject2.dto.person.PersonInfoInDto;
import shop.mtcoding.miniproject2.dto.person.PersonReq.JoinPersonReqDto;
import shop.mtcoding.miniproject2.dto.person.PersonReq.LoginPersonReqDto;
import shop.mtcoding.miniproject2.dto.person.PersonRespDto.JoinPersonRespDto;
import shop.mtcoding.miniproject2.dto.person.PersonRespDto.JoinPersonRespDto.SkillDto;
import shop.mtcoding.miniproject2.dto.person.PersonRespDto.JoinPersonRespDto.UserDto;
import shop.mtcoding.miniproject2.dto.post.PostRecommendOutDto.PostRecommendIntegerRespDto;
import shop.mtcoding.miniproject2.dto.post.PostRecommendOutDto.PostRecommendTimeStampResDto;
import shop.mtcoding.miniproject2.dto.user.UserLoginDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
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
import shop.mtcoding.miniproject2.util.JwtProvider;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final SkillRepository skillRepository;
    private final PersonRepository personRepository;
    private final HttpSession session;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SkillFilterRepository skillFilterRepository;
    private final PersonScrapRepository personScrapRepository;

    @Transactional
    public JoinPersonRespDto 개인회원가입(JoinPersonReqDto joinPersonReqDto) {
        // System.out.println(salt);
        Person samePerson = personRepository.findByPersonNameAndEmail(joinPersonReqDto.getName(),
                joinPersonReqDto.getEmail());
        if (samePerson != null) {
            throw new CustomApiException("이미 가입되어 있는 회원입니다.");
        }

        Person person = new Person();
        person.setName(joinPersonReqDto.getName());
        int result = personRepository.insert(person); // joinReqDto(인수)를 매핑
        if (result != 1) {
            throw new CustomApiException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Hash + Salt 다이제스트
        String salt = EncryptionUtils.getSalt();
        joinPersonReqDto
                .setPassword(EncryptionUtils.encrypt(joinPersonReqDto.getPassword(), salt));

        User user = new User(joinPersonReqDto.getEmail(), joinPersonReqDto.getPassword(), salt, person.getId(), 0);

        int result2 = userRepository.insert(user);
        if (result2 != 1) {
            throw new CustomApiException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int result3 = skillRepository.insert(person.getId(), 0, 0, joinPersonReqDto.getSkills());
        if (result3 != 1) {
            throw new CustomApiException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Person personPS = personRepository.findById(person.getId());
        User userPS = userRepository.findById(user.getId());
        Skill skillPS = skillRepository.findByPInfoId(person.getId());
        JoinPersonRespDto dto = new JoinPersonRespDto(personPS.getId(), personPS.getName(),
                new UserDto(userPS.getId(), userPS.getEmail(), userPS.getCreatedAt()),
                new SkillDto(skillPS.getId(), skillPS.getSkills()));

        return dto;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> 개인로그인(LoginPersonReqDto loginPersonReqDto) {
        User userCheck = userRepository.findByEmail(loginPersonReqDto.getEmail());
        if (userCheck == null) {
            throw new CustomApiException("이메일 혹은 패스워드가 잘못입력되었습니다.");
        }
        // DB Salt 값
        String salt = userCheck.getSalt();

        // DB Salt + 입력된 password 해싱
        loginPersonReqDto.setPassword(EncryptionUtils.encrypt(loginPersonReqDto.getPassword(), salt));
        User principal = userRepository.findPersonByEmailAndPassword(loginPersonReqDto.getEmail(),
                loginPersonReqDto.getPassword());
        if (principal == null) {
            throw new CustomApiException("이메일 혹은 패스워드가 잘못입력되었습니다.");
        }
        // jwt 생성
        String jwt = JwtProvider.create(principal);

        // header에 담기
        ResponseEntity<Object> response = new ResponseEntity<>(new ResponseDto<>(1, "로그인 완료", null),
                HttpStatus.OK);

        HttpHeaders headers = new HttpHeaders();
        headers.putAll(response.getHeaders());
        headers.add(JwtProvider.HEADER, jwt);

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(response.getBody(), headers,
                response.getStatusCode());

        return responseEntity;
    }

    @Transactional
    public void update(PersonInfoInDto personInfoInDto) {

        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");

        User userPs = userRepository.findById(principal.getId());

        Person personPS = personRepository.findById(principal.getPInfoId());

        if (personPS == null) {
            throw new CustomApiException("정보를 찾을 수 없습니다!");
        }

        String password;
        String pw = EncryptionUtils.encrypt(personInfoInDto.getOriginPassword(), userPs.getSalt());
        if (!pw.equals(userPs.getPassword())) {
            throw new CustomApiException("비밀번호가 일치하지 않습니다!");
        }

        if (personInfoInDto.getPassword() == null || personInfoInDto.getPassword().isEmpty()) {
            password = userPs.getPassword();
        } else {
            password = EncryptionUtils.encrypt(personInfoInDto.getPassword(), userPs.getSalt());
        }

        Timestamp birthday = Timestamp.valueOf(personInfoInDto.getBirthday());
        int result = personRepository.updateById(personPS.getId(), personInfoInDto.getName(),
                personInfoInDto.getPhone(),
                personInfoInDto.getAddress(), birthday);

        if (result != 1) {
            throw new CustomApiException("정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Skill skillPS = skillRepository.findByPInfoId(personPS.getId());

        if (skillPS == null) {
            throw new CustomApiException("정보를 찾을 수 없습니다");
        }

        int result2 = skillRepository.updateById(skillPS.getId(), personPS.getId(), 0, 0, personInfoInDto.getSkills(),
                skillPS.getCreatedAt());

        if (result2 != 1) {
            throw new CustomApiException("정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int result3 = userRepository.updateById(userPs.getId(), userPs.getEmail(), password,
                userPs.getPInfoId(),
                userPs.getCInfoId(), userPs.getCreatedAt());

        if (result3 != 1) {
            throw new CustomApiException("정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<PostRecommendIntegerRespDto> recommend() {
        UserLoginDto principal = (UserLoginDto) session.getAttribute("principal");
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
                throw new CustomApiException("추천 공고 불러오기 실패");
            }
        }

        return postList;
    }

}

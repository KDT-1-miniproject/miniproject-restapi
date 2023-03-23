package shop.mtcoding.miniproject2.service;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.miniproject2.dto.person.PersonInfoInDto;
import shop.mtcoding.miniproject2.dto.person.PersonReq.JoinPersonReqDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.Person;
import shop.mtcoding.miniproject2.model.PersonRepository;
import shop.mtcoding.miniproject2.model.Skill;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.model.UserRepository;
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
    public void update(PersonInfoInDto personInfoInDto) {

        User principal = (User) session.getAttribute("principal");
        Person personPS = personRepository.findById(principal.getPInfoId());
        String password;

        String pw = EncryptionUtils.encrypt(personInfoInDto.getOriginPassword(), principal.getSalt());
        if (!pw.equals(principal.getPassword())) {
            throw new CustomApiException("비밀번호가 일치하지 않습니다!");
        }

        if (personInfoInDto.getPassword() == null || personInfoInDto.getPassword().isEmpty()) {
            password = principal.getPassword();
        } else {
            password = EncryptionUtils.encrypt(personInfoInDto.getPassword(), principal.getSalt());
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

        int result3 = userRepository.updateById(principal.getId(), principal.getEmail(), password,
                principal.getPInfoId(),
                principal.getCInfoId(), personPS.getCreatedAt());

        if (result3 != 1) {
            throw new CustomApiException("정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

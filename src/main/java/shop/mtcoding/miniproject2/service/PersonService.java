package shop.mtcoding.miniproject2.service;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.person.PersonInfoInDto;
import shop.mtcoding.miniproject2.dto.person.PersonReq.JoinPersonReqDto;
import shop.mtcoding.miniproject2.dto.person.PersonReq.LoginPersonReqDto;
import shop.mtcoding.miniproject2.dto.person.PersonRespDto.JoinPersonRespDto;
import shop.mtcoding.miniproject2.dto.person.PersonRespDto.JoinPersonRespDto.SkillDto;
import shop.mtcoding.miniproject2.dto.person.PersonRespDto.JoinPersonRespDto.UserDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.Person;
import shop.mtcoding.miniproject2.model.PersonRepository;
import shop.mtcoding.miniproject2.model.Skill;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.model.UserRepository;
import shop.mtcoding.miniproject2.util.EncryptionUtils;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final SkillRepository skillRepository;
    private final PersonRepository personRepository;
    private final HttpSession session;
    private final UserRepository userRepository;

    @Transactional
    public JoinPersonRespDto 개인회원가입(JoinPersonReqDto joinPersonReqDto) {
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

        User user = new User(joinPersonReqDto.getEmail(), joinPersonReqDto.getPassword(), salt, person.getId(), 0);

        int result2 = userRepository.insert(user);
        if (result2 != 1) {
            throw new CustomException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int result3 = skillRepository.insert(person.getId(), 0, 0, joinPersonReqDto.getSkills());
        if (result3 != 1) {
            throw new CustomException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Person personPS = personRepository.findById(person.getId());
        User userPS = userRepository.findById(user.getId());
        Skill skillPS = skillRepository.findByPInfoId(person.getId());
        JoinPersonRespDto dto = new JoinPersonRespDto(personPS.getId(), personPS.getName(),
                new UserDto(userPS.getId(), userPS.getEmail(), userPS.getCreatedAt()),
                new SkillDto(skillPS.getId(), skillPS.getSkills()));

        return dto;
    }

    @Transactional
    public User 개인로그인(LoginPersonReqDto loginPersonReqDto) {
        User userCheck = userRepository.findByEmail(loginPersonReqDto.getEmail());
        if (userCheck == null) {
            throw new CustomException("이메일 혹은 패스워드가 잘못입력되었습니다1.");
        }
        // DB Salt 값
        String salt = userCheck.getSalt();
        // DB Salt + 입력된 password 해싱
        loginPersonReqDto.setPassword(EncryptionUtils.encrypt(loginPersonReqDto.getPassword(), salt));
        User principal = userRepository.findPersonByEmailAndPassword(loginPersonReqDto.getEmail(),
                loginPersonReqDto.getPassword());
        if (principal == null) {
            throw new CustomException("이메일 혹은 패스워드가 잘못입력되었습니다2.");
        }

        return principal;
    }

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

}

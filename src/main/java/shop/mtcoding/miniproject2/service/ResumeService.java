package shop.mtcoding.miniproject2.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeUpdateReqDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.Person;
import shop.mtcoding.miniproject2.model.PersonRepository;
import shop.mtcoding.miniproject2.model.Resume;
import shop.mtcoding.miniproject2.model.ResumeRepository;
import shop.mtcoding.miniproject2.model.Skill;
import shop.mtcoding.miniproject2.model.SkillFilterRepository;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.util.CvTimestamp;
import shop.mtcoding.miniproject2.util.PathUtil;

@Transactional
@Service
public class ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private SkillFilterRepository skillFilterRepository;

    public void insertNewResume(int pInfoId, ResumeUpdateReqDto resumeUpdateReqDto) {

        String uuidImageName = PathUtil.writeImageFile(resumeUpdateReqDto.getProfile());
        Timestamp birthday = CvTimestamp.convertStringToTimestamp(resumeUpdateReqDto.getBirthday());

        Resume resume = new Resume(resumeUpdateReqDto);
        resume.setProfile(uuidImageName);
        resume.setPInfoId(pInfoId);
        Person person = new Person(resumeUpdateReqDto);
        person.setBirthday(birthday);
        Skill skill = new Skill(resumeUpdateReqDto);

        int result1 = resumeRepository.insert(resume);
        if (result1 != 1) {
            throw new CustomException("이력서 저장에 문제가 생겼네요", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Person personPS = personRepository.findById(resume.getPInfoId());
        int result2 = personRepository.updateById(resume.getPInfoId(), person.getName(), person.getPhone(),
                person.getAddress(),
                person.getBirthday(), personPS.getCreatedAt());
        if (result2 != 1) {
            throw new CustomException("이력서 저장에 문제가 생겼네요", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int result3 = skillRepository.insert(0, 0, resume.getId(), skill.getSkills());
        if (result3 != 1) {
            throw new CustomException("이력서 저장에 문제가 생겼네요", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            String skills = skill.getSkills();
            String[] skillArr = skills.split(",");
            for (int i = 0; i < skillArr.length; i++) {
                skillFilterRepository.insert(skillArr[i], 0, resume.getId());
            }
        } catch (Exception e) {
            throw new CustomException("이력서 등록 실패.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void updateById(int id, int pInfoId, ResumeUpdateReqDto resumeUpdateReqDto) {
        String uuidImageName = PathUtil.writeImageFile(resumeUpdateReqDto.getProfile());
        Timestamp birthday = CvTimestamp.convertStringToTimestamp(resumeUpdateReqDto.getBirthday());

        Resume resume = new Resume(resumeUpdateReqDto);

        resume.setProfile(uuidImageName);
        resume.setPInfoId(pInfoId);
        Person person = new Person(resumeUpdateReqDto);
        person.setBirthday(birthday);
        Skill skill = new Skill(resumeUpdateReqDto);

        Resume resumePS = resumeRepository.findById(id);
        int result1 = resumeRepository.updateById(id, pInfoId, resume.getProfile(), resume.getTitle(),
                resume.isPublish(), resume.getPortfolio(), resume.getSelfIntro(), resumePS.getCreatedAt());
        if (result1 != 1) {
            throw new CustomException("이력서 저장에 문제가 생겼네요", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Person personPS = personRepository.findById(resume.getPInfoId());
        int result2 = personRepository.updateById(resume.getPInfoId(), person.getName(), person.getPhone(),
                person.getAddress(),
                person.getBirthday(), personPS.getCreatedAt());
        if (result2 != 1) {
            throw new CustomException("이력서 저장에 문제가 생겼네요", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Skill skillPS = skillRepository.findByPInfoId(resume.getPInfoId());
        int result3 = skillRepository.updateById(skillPS.getId(), resume.getPInfoId(), 0, 0, skill.getSkills(),
                skillPS.getCreatedAt());
        if (result3 != 1) {
            throw new CustomException("이력서 저장에 문제가 생겼네요", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // skill filter 삭제 후 다시 저장

        try {
            skillFilterRepository.deleteByResumeId(resumePS.getId());
        } catch (Exception e) {
            throw new CustomException("이력서 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            String[] skillArr = skillPS.getSkills().split(",");
            for (int i = 0; i < skillArr.length; i++) {
                skillFilterRepository.insert(skillArr[i], 0, resumePS.getId());
            }
        } catch (Exception e) {
            throw new CustomException("이력서 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void delete(int id) {
        int result = resumeRepository.deleteById(id);
        if (result != 1) {
            throw new CustomApiException("이력서 삭제 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            skillRepository.deleteByResumeId(id);
        } catch (Exception e) {
            throw new CustomApiException("이력서 삭제 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            skillFilterRepository.deleteByResumeId(id);
        } catch (Exception e) {
            throw new CustomApiException("이력서 삭제 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

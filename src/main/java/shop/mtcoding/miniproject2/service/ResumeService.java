package shop.mtcoding.miniproject2.service;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeInsertReqBirthdayTimestampDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeInsertReqDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeUpdateReqBirthdayTimestampDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeUpdateReqDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRes.ResumeDetailDto;
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

@AllArgsConstructor
@Transactional
@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private final PersonRepository personRepository;

    private final SkillRepository skillRepository;

    private final SkillFilterRepository skillFilterRepository;

    public Resume insertNewResume(int pInfoId, ResumeInsertReqDto resumeInsertReqDto) {

        String uuidImageName = PathUtil.writeImageFile(resumeInsertReqDto.getProfile());
        Timestamp birthday = CvTimestamp.convertStringToTimestamp(resumeInsertReqDto.getBirthday());
        ResumeInsertReqBirthdayTimestampDto resumeInsertReqBirthdayTimestampDto = new ResumeInsertReqBirthdayTimestampDto(
                pInfoId,
                resumeInsertReqDto.getTitle(),
                resumeInsertReqDto.getPortfolio(),
                resumeInsertReqDto.isPublish(),
                resumeInsertReqDto.getSelfIntro(),
                resumeInsertReqDto.getName(),
                resumeInsertReqDto.getPhone(),
                resumeInsertReqDto.getAddress(),
                resumeInsertReqDto.getSkills());
        resumeInsertReqBirthdayTimestampDto.setProfile(uuidImageName);
        resumeInsertReqBirthdayTimestampDto.setBirthday(birthday);

        int result1 = resumeRepository.insert(resumeInsertReqBirthdayTimestampDto);
        if (result1 != 1) {
            throw new CustomException("이력서 저장에 문제가 생겼네요1", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        int resumeIdDb = resumeInsertReqBirthdayTimestampDto.getResumeId();
        // System.out.println("resumeId" + resumeIdDb);
        int result2 = personRepository.updateById(pInfoId, resumeInsertReqBirthdayTimestampDto.getName(),
                resumeInsertReqBirthdayTimestampDto.getPhone(),
                resumeInsertReqBirthdayTimestampDto.getAddress(),
                resumeInsertReqBirthdayTimestampDto.getBirthday());
        if (result2 != 1) {
            throw new CustomException("이력서 저장에 문제가 생겼네요2", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int result3 = skillRepository.insert(0, 0, resumeIdDb,
                resumeInsertReqBirthdayTimestampDto.getSkills());
        if (result3 != 1) {
            throw new CustomException("이력서 저장에 문제가 생겼네요3", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // skill filter 삭제 후 다시 저장

        try {
            String[] skillArr = resumeInsertReqBirthdayTimestampDto.getSkills().split(",");
            for (int i = 0; i < skillArr.length; i++) {
                skillFilterRepository.insert(skillArr[i], 0, resumeIdDb);
            }
        } catch (Exception e) {
            throw new CustomApiException("이력서 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Resume dto = resumeRepository.findById(resumeIdDb);
        // String datacheck = skillRepository.findByResumeId(resumeIdDb).getSkills();
        // System.out.println(datacheck);
        return dto;
    }

    public void updateById(int id, int pInfoId, ResumeUpdateReqDto resumeUpdateReqDto) {
        String uuidImageName = PathUtil.writeImageFile(resumeUpdateReqDto.getProfile());
        Timestamp birthday = CvTimestamp.convertStringToTimestamp(resumeUpdateReqDto.getBirthday());

        ResumeUpdateReqBirthdayTimestampDto resumeUpdateReqBirthdayTimestampDto = new ResumeUpdateReqBirthdayTimestampDto(
                resumeUpdateReqDto.getTitle(),
                resumeUpdateReqDto.getPortfolio(),
                resumeUpdateReqDto.isPublish(),
                resumeUpdateReqDto.getSelfIntro(),
                resumeUpdateReqDto.getName(),
                resumeUpdateReqDto.getPhone(),
                resumeUpdateReqDto.getAddress(),
                resumeUpdateReqDto.getSkills());
        resumeUpdateReqBirthdayTimestampDto.setProfile(uuidImageName);
        resumeUpdateReqBirthdayTimestampDto.setBirthday(birthday);

        int result1 = resumeRepository.updateById(id, pInfoId,
                resumeUpdateReqBirthdayTimestampDto.getProfile(), resumeUpdateReqBirthdayTimestampDto.getTitle(),
                resumeUpdateReqBirthdayTimestampDto.isPublish(), resumeUpdateReqBirthdayTimestampDto.getPortfolio(),
                resumeUpdateReqBirthdayTimestampDto.getSelfIntro());
        if (result1 != 1) {
            throw new CustomException("이력서 저장에 문제가 생겼네요", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Skill skillPS = skillRepository.findByResumeId(id);
        int result3 = skillRepository.updateById(skillPS.getId(), 0, 0, id,
                resumeUpdateReqBirthdayTimestampDto.getSkills(),
                skillPS.getCreatedAt());
        if (result3 != 1) {
            throw new CustomException("이력서 저장에 문제가 생겼네요", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // skill filter 삭제 후 다시 저장

        try {
            skillFilterRepository.deleteByResumeId(id);
        } catch (Exception e) {
            throw new CustomApiException("이력서 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            String[] skillArr = skillPS.getSkills().split(",");
            for (int i = 0; i < skillArr.length; i++) {
                skillFilterRepository.insert(skillArr[i], 0, id);
            }
        } catch (Exception e) {
            throw new CustomApiException("이력서 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
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

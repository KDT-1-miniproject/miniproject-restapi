package shop.mtcoding.miniproject2.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendDto;

import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeInsertReqBirthdayTimestampDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRes.ResumeDetailDto;

@Mapper
public interface ResumeRepository {
        public List<Resume> findAll();

        public List<Resume> findAllByPInfoId(int pInfoId);

        public ResumeRecommendDto findNameAndTitleAndSkills(int resumeId);

        public Resume findById(int id);

        public ResumeDetailDto findDetailList(int id);

        public int insert(ResumeInsertReqBirthdayTimestampDto resumeInsertReqBirthdayTimestampDto);

        public int updateById(@Param("id") int id,
                        @Param("pInfoId") int pInfoId, @Param("profile") String profile,
                        @Param("title") String title, @Param("publish") boolean publish,
                        @Param("portfolio") String portfolio, @Param("selfIntro") String selfIntro);

        public int deleteById(int id);

}

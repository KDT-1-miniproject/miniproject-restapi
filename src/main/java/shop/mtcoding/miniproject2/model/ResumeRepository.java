package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendDto;

@Mapper
public interface ResumeRepository {
        public List<Resume> findAll();

        public List<Resume> findAllByPInfoId(int pInfoId);

        public ResumeRecommendDto findNameAndTitleAndSkills(int resumeId);

        public Resume findById(int id);

        public int insert(Resume resume);

        // public int insert(@Param("pInfoId") int pInfoId, @Param("profile") String
        // profile,
        // @Param("title") String title,
        // @Param("portfolio") String portfolio, @Param("selfIntro") String selfIntro);
        // skill은 따로 insert 해줘서 관리해줘야함!

        public int updateById(@Param("id") int id,
                        @Param("pInfoId") int pInfoId, @Param("profile") String profile,
                        @Param("title") String title, @Param("publish") boolean publish,
                        @Param("portfolio") String portfolio, @Param("selfIntro") String selfIntro,
                        @Param("createdAt") Timestamp createdAt);

        public int deleteById(int id);

}

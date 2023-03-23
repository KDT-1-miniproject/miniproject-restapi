package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.miniproject2.dto.Resume.ResumeRes.ResumeDetailDto;

@Mapper
public interface SkillRepository {

        public List<Skill> findAll();

        public Skill findById(int id);

        public Skill findByPostId(int postId);

        public Skill findByPInfoId(int pInfoId);

        public Skill findByResumeId(int resumeId);

        public int insert(@Param("pInfoId") int pInfoId, @Param("postId") int postId,
                        @Param("resumeId") int resumeId,
                        @Param("skills") String skills);

        public int updateById(@Param("id") int id,
                        @Param("pInfoId") int pInfoId,
                        @Param("postId") int postId,
                        @Param("resumeId") int resumeId,
                        @Param("skills") String skills,
                        @Param("createdAt") Timestamp createdAt);

        public int deleteById(int id);

        public int deleteByPostId(int postId);

        public int deleteByResumeId(int resumeId);
}

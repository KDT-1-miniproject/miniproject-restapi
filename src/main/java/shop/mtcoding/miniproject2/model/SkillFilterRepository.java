package shop.mtcoding.miniproject2.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SkillFilterRepository {

        public List<SkillFilter> findAll();

        public SkillFilter findById(int id);

        public List<SkillFilter> findSkillNameForPerson(String skill);

        public List<SkillFilter> findSkillNameForCompany(String skill);

        public List<SkillFilter> findByResumeId(int resumeId);

        public int insert(@Param("skill") String skill, @Param("postId") int postId,
                        @Param("resumeId") int resumeId);

        public int updateById(@Param("id") int id,
                        @Param("skill") String skill,
                        @Param("postId") int postId,
                        @Param("resumeId") int resumeId);

        public int deleteById(int id);

        public int deleteByResumeId(int resumeId);

        public int deleteByPostId(int postId);
}

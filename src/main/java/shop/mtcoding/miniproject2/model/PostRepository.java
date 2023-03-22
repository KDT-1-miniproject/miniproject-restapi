package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.miniproject2.dto.post.PostRecommendOutDto.PostRecommendTimeStampResDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostMainRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostTitleRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.postIdAndSkillsDto;

@Mapper
public interface PostRepository {
        public List<Post> findAll();

        public Post findById(int id);

        public List<PostTitleRespDto> findAllTitleByCInfoId(int cInfoId);

        public List<postIdAndSkillsDto> findPostIdAndSkills(int cInfoid);

        public List<PostMainRespDto> findAllWithCInfo();

        public PostRecommendTimeStampResDto findByPostIdToRecmmend(int postId);

        public int insert(Post post);
        // @Param("title") String title, @Param("cInfoId") int cInfoId,
        // @Param("career") int career,
        // @Param("pay") String pay, @Param("condition") String condition,
        // @Param("startHour") Time startHour,
        // @Param("endHour") Time endHour, @Param("deadline") Timestamp deadline,
        // @Param("cIntro") String cIntro,
        // @Param("jobIntro") String jobIntro
        // skill은 따로 insert 해줘서 관리해줘야함!

        public int updateById(
                        @Param("id") int id,
                        @Param("title") String title, @Param("cInfoId") int cInfoId,
                        @Param("career") int career,
                        @Param("pay") String pay, @Param("condition") String condition,
                        @Param("startHour") String startHour,
                        @Param("endHour") String endHour, @Param("deadline") Timestamp deadline,
                        @Param("cIntro") String cIntro,
                        @Param("jobIntro") String jobIntro,
                        @Param("createdAt") Timestamp createdAt);

        public int deleteById(int id);

}

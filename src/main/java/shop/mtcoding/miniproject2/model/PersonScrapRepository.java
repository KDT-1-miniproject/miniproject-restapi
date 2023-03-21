package shop.mtcoding.miniproject2.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.miniproject2.dto.personScrap.PersonScrapOutDto;
import shop.mtcoding.miniproject2.dto.personScrap.PersonScrapResDto.PersonScrapTimeStampResDto;

@Mapper
public interface PersonScrapRepository {
        public List<PersonScrap> findAll();

        public List<PersonScrapOutDto> findByIdWithPostAndCompany(int id);

        public PersonScrap findById(int id);

        public List<PersonScrapTimeStampResDto> findByPInfoId(int pInfoId);

        public PersonScrap findByPInfoIdAndPostId(@Param("pInfoId") int pInfoId,
                        @Param("postId") int postId);

        public int insert(@Param("pInfoId") int pInfoId,
                        @Param("postId") int postId);

        public int updateById(@Param("id") int id,
                        @Param("pInfoId") int pInfoId,
                        @Param("postId") int postId);

        public int deleteById(int id);
}

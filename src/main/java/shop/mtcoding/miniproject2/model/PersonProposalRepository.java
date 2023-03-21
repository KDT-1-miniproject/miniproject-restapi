package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.CompanyProposalListRespDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.PersonProposalDetailRespDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.PersonProposalListRespDto;

@Mapper
public interface PersonProposalRepository {
        public List<PersonProposal> findAll();

        public PersonProposal findById(int id);

        public List<PersonProposalListRespDto> findAllWithPostAndCInfoByPInfoId(int pInfoId);

        public List<CompanyProposalListRespDto> findAllWithPostAndResumeAndPInfoByCInfoId(int cInfoId);

        public List<PersonProposalDetailRespDto> findAllWithPostByCInfoIdAndResumeId(@Param("cInfoId") int cInfoId,
                        @Param("resumeId") int resumeId);

        public int insert(@Param("pInfoId") int pInfoId,
                        @Param("postId") int postId,
                        @Param("resumeId") int resumeId,
                        @Param("status") int status // 0은 대기중, 1은 합격, 2는 불합격
        );

        public int updateById(@Param("id") int id,
                        @Param("pInfoId") int pInfoId,
                        @Param("postId") int postId,
                        @Param("resumeId") int resumeId,
                        @Param("status") int status,
                        @Param("createdAt") Timestamp createdAt);

        public int deleteById(int id);

        public PersonProposal findByPInfoIdAndPostId(@Param("pInfoId") int pInfoId, @Param("postId") int postId);

}

package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProposalPassRepository {
        public List<ProposalPass> findAll();

        public ProposalPass findById(int id);

        public List<ProposalPass> findAllByPInfoId(int pInfoId);

        public int insert(ProposalPass proposalPass);

        public int updateById(@Param("id") int id, @Param("pInfoId") int pInfoId,
                        @Param("pProposalId") int pProposalId, @Param("comment") String comment,
                        @Param("createdAt") Timestamp createdAt);

        public int deleteById(int id);

}

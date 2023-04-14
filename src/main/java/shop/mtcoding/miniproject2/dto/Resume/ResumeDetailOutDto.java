package shop.mtcoding.miniproject2.dto.Resume;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRes.ResumeDetailDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.PersonProposalDetailRespDto;

@Getter
@Setter
public class ResumeDetailOutDto {

    private ResumeDetailDto resumeDetail;
    private List<PersonProposalDetailRespDto> proposal;

    public ResumeDetailOutDto(ResumeDetailDto resumeDetail, List<PersonProposalDetailRespDto> proposal) {
        this.resumeDetail = resumeDetail;
        this.proposal = proposal;
    }

}

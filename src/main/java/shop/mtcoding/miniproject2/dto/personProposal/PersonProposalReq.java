package shop.mtcoding.miniproject2.dto.personProposal;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

public class PersonProposalReq {
    @Getter
    @Setter
    public static class CompanyProposalStatusReqDto {
        @NotEmpty
        private String statusCode;
    }
}

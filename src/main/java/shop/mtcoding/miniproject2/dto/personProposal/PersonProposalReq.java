package shop.mtcoding.miniproject2.dto.personProposal;

import lombok.Getter;
import lombok.Setter;

public class PersonProposalReq {
    @Getter
    @Setter
    public static class CompanyProposalStatusReqDto {
        private int statusCode;
    }
}

package shop.mtcoding.miniproject2.dto.proposalPass;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

public class ProposalPassReq {
    @Getter
    @Setter
    public static class ProposalPassMessageReqDto {
        @NotEmpty(message = "메시지를 확인해주세요")
        private String message;
    }
}

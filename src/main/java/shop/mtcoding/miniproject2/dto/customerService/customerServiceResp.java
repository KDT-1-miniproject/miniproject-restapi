package shop.mtcoding.miniproject2.dto.customerService;

import lombok.Getter;
import lombok.Setter;

public class customerServiceResp {

    @Getter
    @Setter
    public static class PersonCustomerServiceRespDto {
        private String question;
        private String answer;
    }

    @Getter
    @Setter
    public static class CompanyCustomerServiceRespDto {
        private String question;
        private String answer;
    }
}

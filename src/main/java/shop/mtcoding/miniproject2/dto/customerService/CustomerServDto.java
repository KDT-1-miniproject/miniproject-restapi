package shop.mtcoding.miniproject2.dto.customerService;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerServDto {
    private Integer id;
    private List<PersonCustomerServiceDto> personCS;
    private List<CompanyCustomerServiceDto> companyCS;

    @Getter
    @Setter
    public static class PersonCustomerServiceDto {
        private String question;
        private String answer;
    }

    @Getter
    @Setter
    public static class CompanyCustomerServiceDto {
        private String question;
        private String answer;
    }
}

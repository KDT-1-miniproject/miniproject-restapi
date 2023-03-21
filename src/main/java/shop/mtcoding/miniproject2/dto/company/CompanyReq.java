package shop.mtcoding.miniproject2.dto.company;

import lombok.Getter;
import lombok.Setter;

public class CompanyReq {

    @Getter
    @Setter
    public static class JoinCompanyReqDto {
        private String name;
        private String address;
        private String number;
        private String managerName;
        private String email;
        private String password;
    }

    @Getter
    @Setter
    public static class LoginCompanyReqDto {
        private String email;
        private String password;
    }
}

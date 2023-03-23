package shop.mtcoding.miniproject2.dto.company;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

public class CompanyReq {

    @Getter
    @Setter
    public static class JoinCompanyReqDto {
        @NotEmpty(message = "회사 이름을 확인해주세요")
        private String name;
        @NotEmpty(message = "주소를 확인해주세요")
        private String address;
        @NotEmpty(message = "사업번호를 확인해주세요")
        private String number;
        @NotEmpty(message = "담당자 이름을 확인해주세요")
        private String managerName;
        @NotEmpty(message = "이메일을 확인해주세요")
        private String email;
        @NotEmpty(message = "비밀번호를 확인해주세요")
        private String password;
    }

    @Getter
    @Setter
    public static class LoginCompanyReqDto {
        @NotEmpty(message = "이메일을 확인해주세요")
        private String email;
        @NotEmpty(message = "비밀번호를 확인해주세요")
        private String password;
    }
}

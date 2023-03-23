package shop.mtcoding.miniproject2.dto.person;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

public class PersonReq {

    @Getter
    @Setter
    public static class JoinPersonReqDto {
        @NotEmpty(message = "이름을 확인해주세요")
        private String name;
        @NotEmpty(message = "이메일을 확인해주세요")
        private String email;
        @NotEmpty(message = "비밀번호를 확인해주세요")
        private String password;
        @NotEmpty(message = "스킬들을 확인해주세요")
        private String skills;
    }

    @Getter
    @Setter
    public static class LoginPersonReqDto {
        @NotEmpty(message = "이메일을 확인해주세요")
        private String email;
        @NotEmpty(message = "비밀번호를 확인해주세요")
        private String password;
    }

}

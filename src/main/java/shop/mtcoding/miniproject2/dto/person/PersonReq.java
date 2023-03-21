package shop.mtcoding.miniproject2.dto.person;

import lombok.Getter;
import lombok.Setter;

public class PersonReq {

    @Getter
    @Setter
    public static class JoinPersonReqDto {
        private String name;
        private String email;
        private String password;
    }

    @Getter
    @Setter
    public static class LoginPersonReqDto {
        private String email;
        private String password;
    }

}

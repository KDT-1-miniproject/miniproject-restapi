package shop.mtcoding.miniproject2.dto.person;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonInfoInDto {
    private String name;
    private String birthday;
    private String phone;
    private String email;
    private String address;
    private String password;
    private String skills;
    private String originPassword;
}

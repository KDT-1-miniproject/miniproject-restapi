package shop.mtcoding.miniproject2.dto.person;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PersonInfoInDto {
    private String name;
    private String birthday;
    private String phone;
    private String email;
    private String address;
    private String password;
    private String skills;
    private String originPassword;

    @Builder
    public PersonInfoInDto(String name, String birthday, String phone, String email, String address, String password,
            String skills, String originPassword) {
        this.name = name;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.password = password;
        this.skills = skills;
        this.originPassword = originPassword;
    }
}

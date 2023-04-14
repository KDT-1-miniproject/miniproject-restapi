package shop.mtcoding.miniproject2.dto.person;

import javax.validation.constraints.NotEmpty;

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
    @NotEmpty(message = "이름을 확인해주세요")
    private String name;
    @NotEmpty(message = "생년월일을 확인해주세요")
    private String birthday;
    @NotEmpty(message = "전화번호를 작성해주세요")
    private String phone;
    @NotEmpty(message = "이메일을 작성해주세요")
    private String email;
    @NotEmpty(message = "주소를 작성해주세요")
    private String address;
    private String password;
    private String skills;
    @NotEmpty(message = "비밀번호를 확인해주세요")
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

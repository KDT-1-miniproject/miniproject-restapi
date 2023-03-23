package shop.mtcoding.miniproject2.dto.company;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompanyInfoInDto {

    private String logo;
    @NotEmpty(message = "대표자 이름을 확인해주세요")
    private String bossName;
    @NotEmpty(message = "사원수를 확인해주세요")
    private String size;
    @NotEmpty(message = "설립년도를 확인해주세요")
    private String cyear;
    @NotEmpty(message = "담당자 이름을 확인해주세요")
    private String managerName;
    @NotEmpty(message = "담장자 번호를 확인해주세요")
    private String managerPhone;
    @NotEmpty(message = "회사 주소를 확인해주세요")
    private String address;
    private String password;
    @NotEmpty(message = "비밀번호를 확인해주세요")
    private String originPassword;

    @Builder
    public CompanyInfoInDto(String logo, String bossName, Integer size, String cyear, String managerName,
            String managerPhone, String address, String password, String originPassword) {
        this.logo = logo;
        this.bossName = bossName;
        this.size = Integer.toString(size);
        this.cyear = cyear;
        this.managerName = managerName;
        this.managerPhone = managerPhone;
        this.address = address;
        this.password = password;
        this.originPassword = originPassword;
    }

}

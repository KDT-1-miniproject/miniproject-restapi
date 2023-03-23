package shop.mtcoding.miniproject2.dto.company;

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
    private String bossName;
    private Integer size;
    private String cyear;
    private String managerName;
    private String managerPhone;
    private String address;
    private String password;
    private String originPassword;

    @Builder
    public CompanyInfoInDto(String logo, String bossName, Integer size, String cyear, String managerName,
            String managerPhone, String address, String password, String originPassword) {
        this.logo = logo;
        this.bossName = bossName;
        this.size = size;
        this.cyear = cyear;
        this.managerName = managerName;
        this.managerPhone = managerPhone;
        this.address = address;
        this.password = password;
        this.originPassword = originPassword;
    }

}

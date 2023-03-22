package shop.mtcoding.miniproject2.dto.company;

import lombok.Getter;
import lombok.Setter;

public class CompanyReqDto {

    @Setter
    @Getter
    public static class CompanyUpdateInfoDto {
        private String logo;
        private String bossName;
        private Integer size;
        private String cyear;
        private String managerName;
        private String managerPhone;
        private String address;
        private String password;
        private String originPassword;
    }
}

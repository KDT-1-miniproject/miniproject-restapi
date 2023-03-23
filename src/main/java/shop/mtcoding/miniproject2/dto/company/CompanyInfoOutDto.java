package shop.mtcoding.miniproject2.dto.company;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyInfoOutDto {
    private Integer id;
    private String logo;
    private String name;
    private String number;
    private String bossName;
    private String address;
    private String managerName;
    private String managerPhone;
    private Integer size;
    private Integer cyear;
    private Timestamp createdAt;
    private UserDto user;

    @Getter
    @Setter
    public static class UserDto {
        private Integer id;
        private String email;
        @JsonIgnore
        private String password;
        private String salt;
        private Integer pInfoId;
        private Integer cInfoId;
        private Timestamp createdAt;
    }
}

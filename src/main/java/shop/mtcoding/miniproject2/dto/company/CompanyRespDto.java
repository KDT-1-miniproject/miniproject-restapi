package shop.mtcoding.miniproject2.dto.company;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.miniproject2.model.Company;

public class CompanyRespDto {

    @Getter
    @Setter
    public static class JoinCompanyRespDto {
        private Integer id;
        private String name;
        private String address;
        private String number;
        private String managerName;
        // private String password;
        private UserDto user;

        public JoinCompanyRespDto(Company company, UserDto user) {
            this.id = company.getId();
            this.name = company.getName();
            this.address = company.getAddress();
            this.number = company.getNumber();
            this.managerName = company.getManagerName();
            this.user = user;
        }

        @Getter
        @Setter
        public static class UserDto {
            private Integer id;
            private String email;
            private Timestamp createdAt;

            public UserDto(Integer id, String email, Timestamp createdAt) {
                this.id = id;
                this.email = email;
                this.createdAt = createdAt;
            }
        }
    }
}

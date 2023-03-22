package shop.mtcoding.miniproject2.dto.person;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonInfoOutDto {
    private Integer id;
    private String name;
    private String phone;
    private String address;
    private Timestamp birthday;
    private Timestamp createdAt;
    private SkillDto skill;
    private UserDto user;

    @Getter
    @Setter
    public static class UserDto {
        private Integer id;
        private String email;
        private String password;
    }

    @Getter
    @Setter
    public static class SkillDto {
        private Integer id;
        private Integer pInfoId;
        private Integer postId;
        private Integer resumeId;
        private String skills;
        private Timestamp createdAt;
    }

}

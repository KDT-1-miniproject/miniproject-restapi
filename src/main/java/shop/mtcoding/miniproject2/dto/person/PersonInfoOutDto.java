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

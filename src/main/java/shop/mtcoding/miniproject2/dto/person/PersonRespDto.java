package shop.mtcoding.miniproject2.dto.person;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

public class PersonRespDto {

    @Getter
    @Setter
    public static class JoinPersonRespDto {
        private Integer id;
        private String name;
        private UserDto user;
        private SkillDto skill;

        public JoinPersonRespDto(Integer id, String name, UserDto user, SkillDto skill) {
            this.id = id;
            this.name = name;
            this.user = user;
            this.skill = skill;
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

        @Getter
        @Setter
        public static class SkillDto {
            private Integer id;
            private String skills;

            public SkillDto(Integer id, String skills) {
                this.id = id;
                this.skills = skills;
            }
        }
    }
}

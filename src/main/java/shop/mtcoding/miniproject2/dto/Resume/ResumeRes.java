package shop.mtcoding.miniproject2.dto.Resume;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

public class ResumeRes {
    @Getter
    @Setter
    public static class ResumeDetailDto {
        private Integer id;
        private String profile;
        private String title;
        private boolean publish;
        private String portfolio;
        private String selfIntro;
        private PersonDto personDto;
        private SkillDto skillDto;

        @Getter
        @Setter
        public static class PersonDto {
            private Integer id;
            private String name;
            private String phone;
            private String address;
            private Timestamp birthday;
            private UserDto userDto;

            @Getter
            @Setter
            public static class UserDto {
                private Integer id;
                private String email;
            }
        }

        @Getter
        @Setter
        public static class SkillDto {
            private Integer id;
            private String skills;
        }
    }
}

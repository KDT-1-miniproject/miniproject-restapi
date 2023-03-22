package shop.mtcoding.miniproject2.dto.Resume;

import java.sql.Timestamp;
import java.util.List;

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
        }

        @Getter
        @Setter
        public static class SkillDto {
            private Integer id;
            private String skills;
        }
    }

    @Getter
    @Setter
    public static class ResumeRecommendDto {
        private Integer id;
        private String name;
        private String title;
        private String skills;
    }

    @Getter
    @Setter
    public static class ResumeRecommendArrDto {
        private Integer id;
        private String name;
        private String title;
        private String[] skills;
        private Integer scrap;
    }

    @Getter
    @Setter
    public static class ResumeWithPostInfoRecommendDto {
        private Integer postId;
        private String title;
        private List<ResumeRecommendArrDto> resumes;
    }

}

package shop.mtcoding.miniproject2.dto.Resume;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRes.ResumeRecommendDto.PersonDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRes.ResumeRecommendDto.SkillDto;

public class ResumeRes {

    @Getter
    @Setter
    public static class ResumeRecommendDto {
        private Integer id;
        private String title;
        private PersonDto person;
        private SkillDto skill;

        @Getter
        @Setter
        public static class PersonDto {
            private Integer id;
            private String name;
        }

        @Getter
        @Setter
        public static class SkillDto {
            private Integer id;
            private String skills;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class ResumeRecommendScrapDto {
        private Integer id;
        private String title;
        private PersonDto person;
        private SkillDto skill;
        private Integer scrap;

        public ResumeRecommendScrapDto(ResumeRecommendDto rDto) {
            this.id = rDto.getId();
            this.title = rDto.getTitle();
            this.person = rDto.getPerson();
            this.skill = rDto.getSkill();
        }

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ResumeWithPostInfoRecommendDto {
        private Integer postId;
        private String title;
        private List<ResumeRecommendScrapDto> resumes;
    }

}

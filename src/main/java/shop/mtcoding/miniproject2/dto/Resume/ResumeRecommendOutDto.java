package shop.mtcoding.miniproject2.dto.Resume;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendArrDto;

public class ResumeRecommendOutDto {

    @Getter
    @Setter
    public static class ResumeRecommendDto {
        private Integer id;
        private String name;
        private String title;
        private String skills;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class ResumeRecommendArrDto {
        private Integer id;
        private String name;
        private String title;
        private String[] skills;
        private Integer scrap;

        public ResumeRecommendArrDto(ResumeRecommendDto resDto) {
            this.id = id;
            this.name = name;
            this.title = title;
            this.skills = resDto.getSkills().split(",");
            this.scrap = scrap;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class ResumeWithPostInfoRecommendDto {
        private Integer postId;
        private String title;
        private List<ResumeRecommendArrDto> resumes;

        public ResumeWithPostInfoRecommendDto(Integer postId, String title, List<ResumeRecommendArrDto> resumes) {
            this.postId = postId;
            this.title = title;
            this.resumes = resumes;
        }
    }

}

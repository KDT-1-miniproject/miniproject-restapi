package shop.mtcoding.miniproject2.dto.Resume;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRecommendOutDto.ResumeRecommendArrDto;

public class ResumeRecommendOutDto {

    @Getter
    @Setter
    @ToString
    public static class ResumeRecommendDto {
        private Integer id;
        private String name;
        private String title;
        private String skills;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class ResumeRecommendArrDto {
        private Integer id;
        private String name;
        private String title;
        private String[] skills;
        private Integer scrap;

        public ResumeRecommendArrDto(ResumeRecommendDto resumeDto) {
            this.id = resumeDto.getId();
            this.name = resumeDto.getName();
            this.title = resumeDto.getTitle();
            this.skills = resumeDto.getSkills().split(",");
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

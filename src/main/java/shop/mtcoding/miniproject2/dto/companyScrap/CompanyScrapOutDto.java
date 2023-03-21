package shop.mtcoding.miniproject2.dto.companyScrap;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyScrapOutDto {
    private Integer id;
    private Integer cInfoId;
    private Timestamp createdAt;
    private ResumeDto resume;

    @Getter
    @Setter
    public static class ResumeDto {
        private Integer id;
        private Integer pInfoId;
        private String profile;
        private String title;
        private boolean publish;
        private String portfolio;
        private String selfIntro;
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
}

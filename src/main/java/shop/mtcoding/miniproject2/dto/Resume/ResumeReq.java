package shop.mtcoding.miniproject2.dto.Resume;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

public class ResumeReq {

    @Getter
    @Setter
    public static class ResumeInsertReqDto {
        @NotEmpty(message = "프로필을 확인해주세요")
        private String profile;
        @NotEmpty(message = "이력서 이름을 확인해주세요")
        private String title;
        @NotEmpty(message = "포트폴리오를 확인해주세요")
        private String portfolio;
        private boolean publish;
        @NotEmpty(message = "자기소개서를 확인해주세요")
        private String selfIntro;
        @NotEmpty(message = "스킬들을 확인해주세요")
        private String skills;
    }

    @Getter
    @Setter
    public static class ResumeInsertReqBirthdayTimestampDto {
        private Integer resumeId;
        private int pInfoId;
        private String profile;
        private String title;
        private String portfolio;
        private boolean publish;
        private String selfIntro;
        private String skills;

        public ResumeInsertReqBirthdayTimestampDto(int pInfoId, String title, String portfolio, boolean publish,
                String selfIntro, String skills) {
            this.pInfoId = pInfoId;
            this.title = title;
            this.portfolio = portfolio;
            this.publish = publish;
            this.selfIntro = selfIntro;
            this.skills = skills;
        }

    }

    @Getter
    @Setter
    public static class ResumeUpdateReqBirthdayTimestampDto {
        private String profile;
        private String title;
        private String portfolio;
        private boolean publish;
        private String selfIntro;
        private String skills;

        public ResumeUpdateReqBirthdayTimestampDto(String title, String portfolio, boolean publish, String selfIntro,
                String skills) {
            this.title = title;
            this.portfolio = portfolio;
            this.publish = publish;
            this.selfIntro = selfIntro;
            this.skills = skills;
        }

    }

    @Getter
    @Setter
    public static class ResumeUpdateReqDto {
        @NotEmpty(message = "프로필을 확인해주세요")
        private String profile;
        @NotEmpty(message = "이력서 이름을 확인해주세요")
        private String title;
        @NotEmpty(message = "포트폴리오를 확인해주세요")
        private String portfolio;
        private boolean publish;
        @NotEmpty(message = "자기소개서를 확인해주세요")
        private String selfIntro;
        @NotEmpty(message = "스킬들을 확인해주세요")
        private String skills;
    }
}

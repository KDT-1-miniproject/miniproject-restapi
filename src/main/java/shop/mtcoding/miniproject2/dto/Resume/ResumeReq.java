package shop.mtcoding.miniproject2.dto.Resume;

import lombok.Getter;
import lombok.Setter;

public class ResumeReq {

    @Getter
    @Setter
    public static class ResumeInsertReqDto {
        private String profile;
        private String title;
        private String portfolio;
        private boolean publish;
        private String selfIntro;
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
        private String profile;
        private String title;
        private String portfolio;
        private boolean publish;
        private String selfIntro;
        private String skills;
    }
}

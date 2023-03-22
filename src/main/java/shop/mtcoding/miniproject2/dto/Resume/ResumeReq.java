package shop.mtcoding.miniproject2.dto.Resume;

import java.sql.Timestamp;

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
        private String name;
        private String phone;
        private String address;
        private String birthday;
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
        private String name;
        private String phone;
        private String address;
        private Timestamp birthday;
        private String skills;

        public ResumeInsertReqBirthdayTimestampDto(int pInfoId, String title, String portfolio, boolean publish,
                String selfIntro,
                String name, String phone, String address, String skills) {
            this.pInfoId = pInfoId;
            this.title = title;
            this.portfolio = portfolio;
            this.publish = publish;
            this.selfIntro = selfIntro;
            this.name = name;
            this.phone = phone;
            this.address = address;
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
        private String name;
        private String phone;
        private String address;
        private Timestamp birthday;
        private String skills;

        public ResumeUpdateReqBirthdayTimestampDto(String title, String portfolio, boolean publish, String selfIntro,
                String name, String phone, String address, String skills) {
            this.title = title;
            this.portfolio = portfolio;
            this.publish = publish;
            this.selfIntro = selfIntro;
            this.name = name;
            this.phone = phone;
            this.address = address;
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
        private String name;
        private String phone;
        private String address;
        private String birthday;
        private String skills;
    }
}

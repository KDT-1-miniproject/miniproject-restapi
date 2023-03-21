package shop.mtcoding.miniproject2.dto.post;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

public class PostResp {

    @Getter
    @Setter
    public static class PostTitleRespDto {
        private Integer id;
        private String title;
        private Date deadline;
    }

    @Getter
    @Setter
    public static class PostMainRespDto {
        private Integer postId;
        private String title;
        private Integer cInfoId;
        private Timestamp deadline;
        private String logo;
        private String name;
        private String address;
    }

    @Getter
    @Setter
    public static class PostMainWithScrapRespDto {
        private Integer postId;
        private String title;
        private Integer cInfoId;
        private Timestamp deadline;
        private String logo;
        private String name;
        private String address;
        private Integer scrap;
    }

    @Getter
    @Setter
    public static class PostRecommendTimeStampResDto {
        private int postId;
        private String title;
        private Timestamp deadline;
        private String logo;
        private String name;
        private String address;
    }

    @Getter
    @Setter
    public static class PostRecommendIntegerRespDto {
        private int postId;
        private String title;
        private Integer deadline;
        private String logo;
        private String name;
        private String address;
        private Integer scrap;
    }

    @Getter
    @Setter
    public static class postIdAndSkillsDto {
        private int postId;
        private String skills;

    }

    @Getter
    @Setter
    public static class PostDtailResDto {
        private Integer id;
        private String title;
        private Integer cInfoId;
        private Integer career;
        private String pay;
        private String condition;
        private String startHour;
        private String endHour;
        private String cIntro;
        private String jobIntro;
        private Integer scrap;
    }

    @Getter
    @Setter
    public static class CompanyPostDetailRespDto {
        private Integer id;
        private String title;
        private CompanyDto company;
        private Integer career;
        private String pay;
        private String condition;
        private String startHour;
        private String endHour;
        private Timestamp deadline;
        private String cIntro;
        private String jobIntro;
        private Timestamp createdAt;
        private SkillDto skills;

        @Getter
        @Setter
        public static class CompanyDto {
            private Integer id;
            private String logo;
            private String name;
            private String bossName;
            private String address;
            private String managerName;
            private String managerPhone;
            private Integer size;
            private Integer cyear;
            private Timestamp createdAt;
            private UserDto user;

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

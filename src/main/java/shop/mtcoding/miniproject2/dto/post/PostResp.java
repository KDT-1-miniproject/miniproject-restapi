package shop.mtcoding.miniproject2.dto.post;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.miniproject2.model.Company;
import shop.mtcoding.miniproject2.model.PersonScrap;
import shop.mtcoding.miniproject2.model.Post;

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
        private Integer id;
        private String title;
        private CompanyDto company;
        private Timestamp deadline;
        private ScrapPS scrap;
        private Timestamp createdAt;

        @Getter
        @Setter
        public static class ScrapPS {
            private Integer id;
            private Integer postId;
            private Timestamp createdAt;
        }

        @Getter
        @Setter
        public static class CompanyDto {
            private Integer id;
            private String logo;
            private String name;
            private String address;
            private Timestamp createdAt;
        }
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
    public static class PersonPostDetailResDto {
        private Integer id;
        private String title;
        private Integer career;
        private String pay;
        private String condition;
        private String startHour;
        private String endHour;
        private String comIntro;
        private String jobIntro;
        private SkillDto skills;
        private CompanyDto company;
        private ScrapDto scrap;
        private Timestamp createdAt;
        private List<ResumeDto> resumes;

        @Getter
        @Setter
        public static class ResumeDto {
            private Integer id;
            private String title;
        }

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

            public CompanyDto(Company company, UserDto user) {
                this.id = company.getId();
                this.logo = company.getLogo();
                this.name = company.getName();
                this.bossName = company.getBossName();
                this.address = company.getAddress();
                this.managerName = company.getManagerName();
                this.managerPhone = company.getManagerPhone();
                this.size = company.getSize();
                this.cyear = company.getCyear();
                this.createdAt = company.getCreatedAt();
                this.user = user;
            }

            @Getter
            @Setter
            public static class UserDto {
                private Integer id;
                private String email;

                public UserDto(Integer id, String email) {
                    this.id = id;
                    this.email = email;
                }
            }
        }

        @Getter
        @Setter
        public static class ScrapDto {
            private Integer id;
            private Integer postId;
            private Timestamp createdAt;

            public ScrapDto(PersonScrap scrap) {
                if (scrap != null) {
                    this.id = scrap.getId();
                    this.createdAt = scrap.getCreatedAt();
                }
            }
        }

        @Getter
        @Setter
        public static class SkillDto {
            private Integer id;
            private String skills;

            public SkillDto(Integer id, String skills) {
                this.id = id;
                this.skills = skills;
            }
        }

        public PersonPostDetailResDto(Post post, CompanyDto company, ScrapDto scrap,
                SkillDto skills, List<ResumeDto> resumes) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.company = company;
            this.career = post.getCareer();
            this.pay = post.getPay();
            this.condition = post.getCondition();
            this.startHour = post.getStartHour();
            this.endHour = post.getEndHour();
            this.comIntro = post.getCIntro();
            this.jobIntro = post.getJobIntro();
            this.scrap = scrap;
            this.skills = skills;
            this.createdAt = post.getCreatedAt();
            this.resumes = resumes;
        }

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

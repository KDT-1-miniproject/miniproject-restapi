package shop.mtcoding.miniproject2.dto.post;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.miniproject2.model.Post;

public class PostReq {
    @Getter
    @Setter
    public static class PostSearchReqDto {
        private String search;
    }

    @Getter
    @Setter
    public static class PostSaveReqDto {
        private String title;
        private Integer career;
        private String pay;
        private String condition;
        private String startHour;
        private String endHour;
        private String deadline;
        private String cIntro;
        private String jobIntro;
        private String[] skills;
    }

    @Getter
    @Setter
    public static class PostSaveDto {
        private Integer id;
        private String title;
        private Integer career;
        private String pay;
        private String condition;
        private String startHour;
        private String endHour;
        private String deadline;
        private String cIntro;
        private String jobIntro;
        // private SkillsDto skill;
        private String skills;

        // @Getter
        // @Setter
        // public static class SkillsDto {
        // private Integer id;
        // private String skills;

        // public SkillsDto(Integer id, String skills) {
        // this.id = id;
        // this.skills = skills;
        // }

        // }

        public PostSaveDto(Post post, String skills) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.career = post.getCareer();
            this.pay = post.getPay();
            this.condition = post.getCondition();
            this.startHour = post.getStartHour();
            this.endHour = post.getEndHour();
            this.deadline = post.getDeadline().toString().split(" ")[0];
            this.cIntro = post.getCIntro();
            this.jobIntro = post.getJobIntro();
            this.skills = skills;

        }

    }

    @Getter
    @Setter
    public static class PostUpdateReqDto {
        private String title;
        private Integer career;
        private String pay;
        private String condition;
        private String startHour;
        private String endHour;
        private String deadline;
        private String comIntro;
        private String jobIntro;
        private String skills;
    }

    @Getter
    @Setter
    public static class PostUpdateDto {
        private Integer id;
        private String title;
        private Integer career;
        private String pay;
        private String condition;
        private String startHour;
        private String endHour;
        private String deadline;
        private String cIntro;
        private String jobIntro;
        private String skills;
        private Timestamp createdAt;

        public PostUpdateDto(Post post, String skills) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.career = post.getCareer();
            this.pay = post.getPay();
            this.condition = post.getCondition();
            this.startHour = post.getStartHour();
            this.endHour = post.getEndHour();
            this.deadline = post.getDeadline().toString().split(" ")[0];
            this.cIntro = post.getCIntro();
            this.jobIntro = post.getJobIntro();
            this.skills = skills;
            this.createdAt = post.getCreatedAt();
        }

    }
}

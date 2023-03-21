package shop.mtcoding.miniproject2.dto.post;

import lombok.Getter;
import lombok.Setter;

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
}

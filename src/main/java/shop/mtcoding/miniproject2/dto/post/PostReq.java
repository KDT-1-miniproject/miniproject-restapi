package shop.mtcoding.miniproject2.dto.post;

import java.sql.Timestamp;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
        @NotEmpty(message = "공고 제목을 확인해주세요")
        @Size(max = 20, message = "제목은 20글자를 넘을 수 없습니다.")
        private String title;
        @NotEmpty(message = "경력을 확인해주세요")
        private String career;
        @NotEmpty(message = "연봉을 확인해주세요")
        private String pay;
        @NotEmpty(message = "근무조건을 확인해주세요")
        private String condition;
        @NotEmpty(message = "출근시간을 확인해주세요")
        private String startHour;
        @NotEmpty(message = "퇴근시간을 확인해주세요")
        private String endHour;
        @NotEmpty(message = "마감시간을 확인해주세요")
        private String deadline;
        @NotEmpty(message = "회사소개를 확인해주세요")
        @Size(max = 200, message = "회사소개는 200글자를 넘을 수 없습니다.")
        private String cIntro;
        @NotEmpty(message = "업무소개를 확인해주세요")
        @Size(max = 200, message = "업무소개는 200글자를 넘을 수 없습니다.")
        private String jobIntro;
        @NotEmpty(message = "스킬들을 확인해주세요")
        @Size(min = 2, max = 5, message = "스킬의 개수는 2개 이상, 5개 이하여야 합니다")
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
        @NotEmpty(message = "공고 제목을 확인해주세요")
        @Size(max = 20, message = "제목은 20글자를 넘을 수 없습니다.")
        private String title;
        @NotEmpty(message = "경력을 확인해주세요")
        private String career;
        @NotEmpty(message = "연봉을 확인해주세요")
        private String pay;
        @NotEmpty(message = "근무조건을 확인해주세요")
        private String condition;
        @NotEmpty(message = "출근시간을 확인해주세요")
        private String startHour;
        @NotEmpty(message = "퇴근시간을 확인해주세요")
        private String endHour;
        @NotEmpty(message = "마감시간을 확인해주세요")
        private String deadline;
        @NotEmpty(message = "회사소개를 확인해주세요")
        @Size(max = 200, message = "회사 소개는 200글자를 넘을 수 없습니다.")
        private String comIntro;
        @NotEmpty(message = "업무소개를 확인해주세요")
        @Size(max = 200, message = "업무 소개는 200글자를 넘을 수 없습니다.")
        private String jobIntro;
        @NotEmpty(message = "스킬들을 확인해주세요")
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

package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostSaveReqDto;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostUpdateReqDto;

@Getter
@Setter
public class Post {
    private Integer id;
    private String title;
    private Integer cInfoId;
    private Integer career;
    private String pay;
    private String condition;
    private String startHour;
    private String endHour;
    private Timestamp deadline;
    private String comIntro;
    private String jobIntro;
    private Timestamp createdAt;

    public Post() {

    };

    public Post(PostSaveReqDto postSaveReqDto, int cInfoId) {

        this.title = postSaveReqDto.getTitle();
        this.cInfoId = cInfoId;
        this.career = Integer.parseInt(postSaveReqDto.getCareer());
        this.pay = postSaveReqDto.getPay();
        this.condition = postSaveReqDto.getCondition();
        this.startHour = postSaveReqDto.getStartHour();
        this.endHour = postSaveReqDto.getEndHour();
        String deadTime = postSaveReqDto.getDeadline() + " 00:00:00";
        this.deadline = Timestamp.valueOf(deadTime);
        this.comIntro = postSaveReqDto.getComIntro();
        this.jobIntro = postSaveReqDto.getJobIntro();
    }

    public static Post postSetting(Post postPS, PostUpdateReqDto postUpdateReqDto, int cInfoId) {
        postPS.title = postUpdateReqDto.getTitle();
        postPS.cInfoId = cInfoId;
        postPS.career = Integer.parseInt(postUpdateReqDto.getCareer());
        postPS.pay = postUpdateReqDto.getPay();
        postPS.condition = postUpdateReqDto.getCondition();
        postPS.startHour = postUpdateReqDto.getStartHour();
        postPS.endHour = postUpdateReqDto.getEndHour();
        String deadTime = postUpdateReqDto.getDeadline() + " 00:00:00";
        postPS.deadline = Timestamp.valueOf(deadTime);
        postPS.comIntro = postUpdateReqDto.getComIntro();
        postPS.jobIntro = postUpdateReqDto.getJobIntro();
        return postPS;
    }
}

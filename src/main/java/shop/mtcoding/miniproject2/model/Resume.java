package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeUpdateReqDto;

@Getter
@Setter
public class Resume {
    private Integer id;
    private Integer pInfoId;
    private String profile;
    private String title;
    private boolean publish;
    private String portfolio;
    private String selfIntro;
    private Timestamp createdAt;

    public Resume() {
    }

    public Resume(ResumeUpdateReqDto resumeUpdateReqDto) {
        this.title = resumeUpdateReqDto.getTitle();
        this.publish = resumeUpdateReqDto.isPublish();
        this.portfolio = resumeUpdateReqDto.getPortfolio();
        this.selfIntro = resumeUpdateReqDto.getSelfIntro();
    }
}

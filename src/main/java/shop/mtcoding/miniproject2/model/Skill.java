package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeUpdateReqDto;

@Getter
@Setter
public class Skill {
    private Integer id;
    private Integer pInfoId;
    private Integer postId; // 0
    private Integer resumeId; // 0
    private String skills;
    private Timestamp createdAt;

    public Skill() {

    }

    public Skill(ResumeUpdateReqDto resumeUpdateReqDto) {
        this.skills = resumeUpdateReqDto.getSkills();
    }

    public static String[] madeSkills() {
        String[] st = { "Java", "Spring", "Html", "Javascript", "Sql", "Android", "React", "Node.js", "Express" };
        return st;
    }
}

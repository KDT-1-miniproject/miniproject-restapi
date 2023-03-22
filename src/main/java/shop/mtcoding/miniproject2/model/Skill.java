package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Skill {
    private Integer id;
    private Integer pInfoId;
    private Integer postId; // 0
    private Integer resumeId; // 0
    private String skills;
    private Timestamp createdAt;

    public static String[] madeSkills() {
        String[] st = { "Java", "Spring", "Html", "Javascript", "Sql", "Android", "React", "Node.js", "Express" };
        return st;
    }
}

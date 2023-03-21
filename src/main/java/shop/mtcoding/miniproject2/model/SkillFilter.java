package shop.mtcoding.miniproject2.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillFilter {
    private Integer id;
    private String skill;
    private Integer postId;
    private Integer resumeId;
}

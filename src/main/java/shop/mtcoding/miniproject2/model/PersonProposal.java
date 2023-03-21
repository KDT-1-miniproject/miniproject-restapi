package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonProposal {
    private int id;
    private int pInfoId;
    private int postId;
    private int resumeId;
    private int status; // 0은 대기중, 1은 합격, 2는 불합격
    private Timestamp createdAt;
}

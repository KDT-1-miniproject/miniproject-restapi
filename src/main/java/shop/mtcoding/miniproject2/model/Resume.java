package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

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
}

package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PersonScrap {
    private int id;
    private int pInfoId;
    private int postId;
    private Timestamp created_at;
}

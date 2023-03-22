package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PersonScrap {
    private int id;
    private int pInfoId;
    private int postId;
    private Timestamp createdAt;

    public PersonScrap(int pInfoId, int postId) {
        this.pInfoId = pInfoId;
        this.postId = postId;
    }


}

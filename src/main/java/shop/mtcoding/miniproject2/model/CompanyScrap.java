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
public class CompanyScrap {
    private int id;
    private int cInfoId;
    private int resumeId;
    private Timestamp createdAt;

    public CompanyScrap(int cInfoId, int resumeId) {
        this.cInfoId = cInfoId;
        this.resumeId = resumeId;
    }
}

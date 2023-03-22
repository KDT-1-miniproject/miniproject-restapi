package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompanyScrap {
    private int id;
    private int cInfoId;
    private int resumeId;
    private Timestamp created_at;
}

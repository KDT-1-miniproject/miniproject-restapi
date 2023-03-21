package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyScrap {
    private int id;
    private int cInfoId;
    private int resumeId;
    private Timestamp created_at;
}

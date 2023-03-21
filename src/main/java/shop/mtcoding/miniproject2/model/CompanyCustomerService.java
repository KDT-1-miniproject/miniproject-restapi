package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyCustomerService {
    private int id;
    private String question;
    private String answer;
    private Timestamp createdAt;
}

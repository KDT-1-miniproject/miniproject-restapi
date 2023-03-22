package shop.mtcoding.miniproject2.dto.person;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonLoginDto {
    private Integer id;
    private String email;
    private int pInfoId;
    private Timestamp createdAt;
}

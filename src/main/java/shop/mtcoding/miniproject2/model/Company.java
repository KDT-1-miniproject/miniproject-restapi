package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Company {
    private Integer id;
    private String logo;
    private String name;
    private String number;
    private String bossName;
    private String address;
    private String managerName;
    private String managerPhone;
    private Integer size;
    private Integer cyear;
    private Timestamp createdAt;
}

package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.miniproject2.dto.Resume.ResumeReq.ResumeUpdateReqDto;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Person {
    private Integer id;
    private String name;
    private String phone;
    private String address;
    private Timestamp birthday;
    private Timestamp createdAt;

    public Person() {

    }

    public Person(ResumeUpdateReqDto resumeUpdateReqDto) {
        this.name = resumeUpdateReqDto.getName();
        this.phone = resumeUpdateReqDto.getPhone();
        this.address = resumeUpdateReqDto.getAddress();
    }

}

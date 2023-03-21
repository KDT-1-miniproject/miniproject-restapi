package shop.mtcoding.miniproject2.model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User implements Serializable {
    private Integer id;
    private String email;
    private String password;
    private String salt;
    private Integer pInfoId; // jsp 에서 el로 접근할 시 .PInfoId 로접근해야함 (이유 모르겟음..)
    private Integer cInfoId; // 마찬가지로 CInfoId
    private Timestamp createdAt;

    public void setUsername(String string) {
    }
}
package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Integer id;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String salt;
    private Integer pInfoId; // jsp 에서 el로 접근할 시 .PInfoId 로접근해야함 (이유 모르겟음..)
    private Integer cInfoId; // 마찬가지로 CInfoId
    private Timestamp createdAt;

    public User(String email, String password, String salt, Integer pInfoId, Integer cInfoId) {
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.pInfoId = pInfoId;
        this.cInfoId = cInfoId;
    }

}
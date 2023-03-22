package shop.mtcoding.miniproject2.dto.user;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.miniproject2.model.User;

@Getter
@Setter
public class UserResDto {
    private Integer id;
    private String email;
    private Timestamp createdAt;
    private Integer pInfoId;

    public Integer getPInfoId() {
        return pInfoId;
    }

    public void setPInfoId(Integer pInfoId) {
        this.pInfoId = pInfoId;
    }

    public UserResDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.pInfoId = user.getPInfoId();
    }

}

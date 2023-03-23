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
    private Integer cInfoId;

    public UserResDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.pInfoId = user.getPInfoId();
        this.cInfoId = user.getCInfoId();
    }

}

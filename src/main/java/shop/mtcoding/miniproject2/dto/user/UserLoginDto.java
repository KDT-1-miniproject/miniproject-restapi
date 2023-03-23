package shop.mtcoding.miniproject2.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserLoginDto {
    private Integer id;
    private Integer cInfoId;
    private Integer pInfoId;
    private String email;

    @Builder
    public UserLoginDto(Integer id, Integer cInfoId, Integer pInfoId, String email) {
        this.id = id;
        this.cInfoId = cInfoId;
        this.pInfoId = pInfoId;
        this.email = email;
    }
}

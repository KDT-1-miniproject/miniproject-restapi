package shop.mtcoding.miniproject2.dto.post;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class PostRecommendOutDto {

    @Getter
    @Setter
    @ToString
    public static class PostRecommendTimeStampResDto {
        private Integer postId;
        private String title;
        private Timestamp deadline;
        private String logo;
        private String name;
        private String address;
    }

    @Getter
    @Setter
    @ToString
    public static class PostRecommendIntegerRespDto {
        private Integer postId;
        private String title;
        private Integer deadline;
        private String logo;
        private String name;
        private String address;
        private Integer scrap;

        public PostRecommendIntegerRespDto(PostRecommendTimeStampResDto recommendDto) {
            this.postId = recommendDto.getPostId();
            this.title = recommendDto.getTitle();
            this.logo = recommendDto.getLogo();
            this.name = recommendDto.getName();
            this.address = recommendDto.getAddress();
        }
    }

}

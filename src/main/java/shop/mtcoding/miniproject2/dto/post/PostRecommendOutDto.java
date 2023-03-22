package shop.mtcoding.miniproject2.dto.post;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import shop.mtcoding.miniproject2.dto.post.PostRecommendOutDto.PostRecommendTimeStampResDto.CompanyDto;

@Getter
@Setter
public class PostRecommendOutDto {

    @Getter
    @Setter
    @ToString
    public static class PostRecommendTimeStampResDto {
        private Integer id;
        private String title;
        private Timestamp deadline;
        private CompanyDto company;

        @Getter
        @Setter
        public static class CompanyDto {
            private Integer id;
            private String logo;
            private String address;
            private String name;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class PostRecommendIntegerRespDto {
        private Integer id;
        private String title;
        private Integer deadline;
        private Integer scrap;
        private CompanyDto company;

        public PostRecommendIntegerRespDto(PostRecommendTimeStampResDto recommendDto) {
            this.id = recommendDto.getId();
            this.title = recommendDto.getTitle();
            this.company = recommendDto.getCompany();
        }

        @Getter
        @Setter
        public static class CompanyDto2 {
            private Integer id;
            private String logo;
            private String address;
            private String name;
        }

    }

}

package shop.mtcoding.miniproject2.dto.personScrap;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonScrapOutDto {
    private Integer id;
    private Integer pInfoId;
    private Integer postId;
    private Timestamp createdAt;
    private JobPostDto post;

    @Getter
    @Setter
    public static class JobPostDto {
        private Integer id;
        private String title;
        private Timestamp deadline;
        private CompanyDto company;

        @Getter
        @Setter
        public static class CompanyDto {
            private Integer id;
            private String logo;
            private String name;
            private String address;
        }

    }
}

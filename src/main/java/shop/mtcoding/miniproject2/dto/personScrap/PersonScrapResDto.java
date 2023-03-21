package shop.mtcoding.miniproject2.dto.personScrap;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

public class PersonScrapResDto {

    @Getter
    @Setter
    public static class PersonScrapTimeStampResDto {
        private Integer id;
        private Integer pInfoId;
        private Integer postId;
        private String title;
        private Timestamp deadline;
        private String logo;
        private String name;
        private String address;
    }

    @Getter
    @Setter
    public static class PersonScrapIntegerResDto {
        private Integer id;
        private Integer pInfoId;
        private Integer postId;
        private String title;
        private Integer deadline;
        private String logo;
        private String name;
        private String address;

    }

}

package shop.mtcoding.miniproject2.dto.companyScrap;

import lombok.Getter;
import lombok.Setter;

public class CompanyScrapResDto {

    @Getter
    @Setter
    public static class CompanyScrapWithResumeInfoResDto {
        private Integer id;
        private Integer resumeId;
        private String name;
        private String title;
        private String skills;
    }

    @Getter
    @Setter
    public static class CompanyScrapWithResumeInfoResArrDto {
        private Integer id;
        private Integer resumeId;
        private String name;
        private String title;
        private String[] skills;
    }
}

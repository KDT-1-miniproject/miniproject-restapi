package shop.mtcoding.miniproject2.dto.personProposal;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

public class PersonProposalResp {
    @Getter
    @Setter
    public static class PersonProposalListRespDto {
        private int id;
        private int pInfoId;
        private int postId;
        private int resumeId;
        private int status;
        private Timestamp createdAt;
        private String title;
        private Timestamp deadline;
        private String name;
    }

    @Getter
    @Setter
    public static class PersonProposalStringListRespDto {
        private int id;
        private int pInfoId;
        private int postId;
        private int resumeId;
        private int status;
        private Timestamp createdAt;
        private String title;
        private String deadline;
        private String name;
    }

    @Getter
    @Setter
    public static class CompanyProposalListRespDto {
        private int id;
        private int pInfoId;
        private int postId;
        private int resumeId;
        private int status;
        private Timestamp createdAt;
        private String ptitle;
        private int cInfoId;
        private String rtitle;
        private String name;
    }

    @Getter
    @Setter
    public static class CompanyProposalListDateRespDto {
        private int id;
        private int pInfoId;
        private int postId;
        private int resumeId;
        private int status;
        private String createdAt;
        private String ptitle;
        private int cInfoId;
        private String rtitle;
        private String name;
    }

    @Getter
    @Setter
    public static class PersonProposalDetailRespDto {
        private int id;
        private int pInfoId;
        private int postId;
        private int resumeId;
        private int status;
        private Timestamp createdAt;
        private String title;
        private int cInfoId;
    }
}

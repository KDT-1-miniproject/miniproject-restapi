package shop.mtcoding.miniproject2.dto.personProposal;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class PersonProposalResp {
    @Getter
    @Setter
    public static class PersonProposalListRespDto {
        private Integer id;
        private Integer status;
        private Integer resumeId;
        private Integer pInfoId;
        private PostDto post;
        private CompanyDto company;
        private ProposalPassDto proposalPass;
        private Timestamp createdAt;

        @Getter
        @Setter
        public static class PostDto {
            private Integer id;
            private String title;
            private String deadline;
        }

        @Getter
        @Setter
        public static class CompanyDto {
            private Integer id;
            private String name;
        }

        @Getter
        @Setter
        public static class ProposalPassDto {
            private Integer id;
            private String message;
        }

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
    public static class CompanyGetResumeDto {
        private List<CompanyProposalListRespDto> proposalList;
        private CompanyDto company;

        public CompanyGetResumeDto(List<CompanyProposalListRespDto> proposalList, CompanyDto company) {
            this.proposalList = proposalList;
            this.company = company;
        }

        @Getter
        @Setter
        public static class CompanyProposalListRespDto {
            private int id;
            private int status;
            private PostDto post;
            private ResumeDto resume;
            private String createdAt;

            @Getter
            @Setter
            public static class PostDto {
                private int id;
                private String title;
            }

            @Getter
            @Setter
            public static class ResumeDto {
                private int id;
                private String title;
                private PersonDto person;

                @Getter
                @Setter
                public static class PersonDto {
                    private int id;
                    private String name;
                }
            }
        }

        @Getter
        @Setter
        @AllArgsConstructor
        public static class CompanyDto {
            private int id;
            private String name;
        }
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

package shop.mtcoding.miniproject2.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.Resume.ResumeDetailOutDto;
import shop.mtcoding.miniproject2.dto.Resume.ResumeRes.ResumeDetailDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.CompanyGetResumeDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.CompanyGetResumeDto.CompanyDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.CompanyGetResumeDto.CompanyProposalListRespDto;
import shop.mtcoding.miniproject2.dto.personProposal.PersonProposalResp.PersonProposalDetailRespDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.Company;
import shop.mtcoding.miniproject2.model.CompanyRepository;
import shop.mtcoding.miniproject2.model.PersonProposal;
import shop.mtcoding.miniproject2.model.PersonProposalRepository;
import shop.mtcoding.miniproject2.model.Post;
import shop.mtcoding.miniproject2.model.PostRepository;
import shop.mtcoding.miniproject2.model.Resume;
import shop.mtcoding.miniproject2.model.ResumeRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class PersonProposalService {

    private final PersonProposalRepository personProposalRepository;
    private final PostRepository postRepository;
    private final ResumeRepository resumeRepository;
    private final CompanyRepository companyRepository;

    public void 제안수정하기(int proposalId, int cInfoId, int status) {

        PersonProposal proposal = personProposalRepository.findById(proposalId);
        if (proposal == null) {
            throw new CustomApiException("없는 제안을 확인 할 수 없습니다.");
        }
        Post post = postRepository.findById(proposal.getPostId());
        if (post == null) {
            throw new CustomApiException("없는 공고에 대한 제안을 확인 할 수 없습니다.");
        }
        if (post.getCInfoId() != cInfoId) {
            throw new CustomApiException("본인의 공고가 아니면 제안을 확인 할 수 없습니다.");
        }
        proposal.setStatus(status);
        try {
            personProposalRepository.updateById(proposal.getId(), proposal.getPInfoId(), proposal.getPostId(),
                    proposal.getResumeId(), proposal.getStatus(), proposal.getCreatedAt());
        } catch (Exception e) {
            throw new CustomApiException("공고 수정할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void 지원하기(int pInfoId, int postId, int resumeId, int status) {

        PersonProposal proposal = personProposalRepository.findByPInfoIdAndPostId(pInfoId, postId);
        if (proposal != null) {
            throw new CustomException("이미 지원한 공고입니다.");
        }

        Resume resume = resumeRepository.findById(resumeId);
        if (resume == null) {
            throw new CustomException("없는 이력서로 지원이 불가합니다.");
        }

        if (pInfoId != resume.getPInfoId()) {
            throw new CustomException("나의 이력서로만 지원이 가능합니다.");
        }

        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new CustomException("없는 공고에 지원할 수 없습니다.");
        }

        try {
            personProposalRepository.insert(pInfoId, postId, resumeId, status);

        } catch (Exception e) {
            throw new CustomException("지원에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public CompanyGetResumeDto 받은이력서보기(int cInfoId) {
        List<CompanyProposalListRespDto> companyProposalList = personProposalRepository
                .findAllWithPostAndResumeAndPInfoByCInfoId(cInfoId);

        for (CompanyProposalListRespDto cpl : companyProposalList) {

            String createdAt = cpl.getCreatedAt();
            // System.out.println(createdAt);
            cpl.setCreatedAt(createdAt.split(" ")[0]);
        }

        Company company = companyRepository.findById(cInfoId);
        CompanyGetResumeDto dto = new CompanyGetResumeDto(companyProposalList,
                new CompanyDto(company.getId(), company.getName()));

        return dto;
    }

    public ResumeDetailOutDto 이력서디테일보기(int resumeId, int cInfoId) {
        Resume resumePS = resumeRepository.findById(resumeId);
        if (resumePS == null) {
            throw new CustomApiException("없는 이력서엔 접근할 수 없습니다.");
        }

        List<PersonProposalDetailRespDto> proposalList = personProposalRepository
                .findAllWithPostByCInfoIdAndResumeId(cInfoId, resumeId);
        if (proposalList.size() > 0) {
            // 해당 이력서로 같은회사 다른 공고에 지원했을 수도 있음.
            proposalList.get(0).getPostId(); // postId를 이용해서 어케 해보자...
        }

        ResumeDetailDto resumeDetailDto = resumeRepository.findDetailList(resumeId);
        ResumeDetailOutDto dto = new ResumeDetailOutDto(resumeDetailDto, proposalList);

        return dto;
    }
}

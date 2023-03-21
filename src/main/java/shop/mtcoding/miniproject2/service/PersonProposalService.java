package shop.mtcoding.miniproject2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.handler.ex.CustomException;
import shop.mtcoding.miniproject2.model.PersonProposal;
import shop.mtcoding.miniproject2.model.PersonProposalRepository;
import shop.mtcoding.miniproject2.model.Post;
import shop.mtcoding.miniproject2.model.PostRepository;
import shop.mtcoding.miniproject2.model.Resume;
import shop.mtcoding.miniproject2.model.ResumeRepository;

@Service
@Transactional
public class PersonProposalService {
    @Autowired
    private PersonProposalRepository personProposalRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ResumeRepository resumeRepository;

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
}

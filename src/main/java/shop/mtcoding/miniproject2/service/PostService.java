package shop.mtcoding.miniproject2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.miniproject2.dto.post.PostReq.PostSaveReqDto;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostUpdateReqDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.Post;
import shop.mtcoding.miniproject2.model.PostRepository;
import shop.mtcoding.miniproject2.model.Skill;
import shop.mtcoding.miniproject2.model.SkillFilterRepository;
import shop.mtcoding.miniproject2.model.SkillRepository;

@Transactional // 여기 붙이면 모든 메서드에 다 붙음
@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private SkillFilterRepository skillFilterRepository;

    public int 공고등록(PostSaveReqDto postSaveReqDto, int cInfoId) {
        Post post = new Post(postSaveReqDto, cInfoId);
        int result = postRepository.insert(post);
        if (result != 1) {
            throw new CustomApiException("공고 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String[] st = postSaveReqDto.getSkills();
        String skills = "";
        for (String string : st) {
            if (!skills.equals(""))
                skills += ",";
            skills += string;
        }
        int result1 = skillRepository.insert(0, post.getId(), 0, skills);
        if (result1 != 1) {
            throw new CustomApiException("공고 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        for (int i = 0; i < st.length; i++) {
            int result2 = skillFilterRepository.insert(st[i], post.getId(), 0);
            if (result2 != 1) {
                throw new CustomApiException("공고 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return post.getId();
    }

    public void 공고수정하기(PostUpdateReqDto postUpdateReqDto, int postId, int cInfoId) {
        Post postPS = postRepository.findById(postId);
        if (postPS == null) {
            throw new CustomApiException("없는 공고를 수정할 수 없습니다.");
        }
        postPS = Post.postSetting(postPS, postUpdateReqDto, cInfoId);

        try {
            postRepository.updateById(postPS.getId(), postPS.getTitle(), postPS.getCInfoId(),
                    postPS.getCareer(), postPS.getPay(), postPS.getCondition(), postPS.getStartHour(),
                    postPS.getEndHour(),
                    postPS.getDeadline(), postPS.getCIntro(), postPS.getJobIntro(), postPS.getCreatedAt());
        } catch (Exception e) {
            throw new CustomApiException("공고 수정할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Skill skillPS = skillRepository.findByPostId(postId);
        String st = postUpdateReqDto.getSkills(); // 수정할 스킬들

        skillPS.setSkills(st);

        int result2 = skillRepository.updateById(skillPS.getId(), skillPS.getPInfoId(), skillPS.getPostId(),
                skillPS.getResumeId(), skillPS.getSkills(), skillPS.getCreatedAt());

        if (result2 != 1) {
            throw new CustomApiException("공고 수정할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // skill filter
        try {
            skillFilterRepository.deleteByPostId(postPS.getId());
        } catch (Exception e) {
            throw new CustomApiException("공고 수정할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            String[] skillArr = st.split(",");
            for (int i = 0; i < skillArr.length; i++) {
                skillFilterRepository.insert(skillArr[i], postId, 0);
            }
        } catch (Exception e) {
            throw new CustomApiException("공고 수정할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void 공고삭제하기(int postId, int cInfoId) {
        Post postPS = postRepository.findById(postId);
        if (postPS == null) {
            throw new CustomApiException("없는 공고를 삭제 할 수 없습니다.");
        }
        if (cInfoId != postPS.getCInfoId()) {
            throw new CustomApiException("공고를 삭제 할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        try {
            postRepository.deleteById(postId);
        } catch (Exception e) {
            throw new CustomApiException("공고를 삭제실패.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            skillRepository.deleteByPostId(postId);
        } catch (Exception e) {
            throw new CustomApiException("공고를 삭제실패.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            skillFilterRepository.deleteByPostId(postPS.getId());
        } catch (Exception e) {
            throw new CustomApiException("공고 삭제실패.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

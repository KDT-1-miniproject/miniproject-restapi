package shop.mtcoding.miniproject2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostSaveDto;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostSaveReqDto;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostUpdateDto;
import shop.mtcoding.miniproject2.dto.post.PostReq.PostUpdateReqDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.CompanyPostDetailRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PersonPostDetailResDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PersonPostDetailResDto.CompanyDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PersonPostDetailResDto.CompanyDto.UserDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PersonPostDetailResDto.ResumeDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PersonPostDetailResDto.ScrapDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PersonPostDetailResDto.SkillDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostMainRespDto;
import shop.mtcoding.miniproject2.dto.post.PostResp.PostTitleRespDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.Company;
import shop.mtcoding.miniproject2.model.CompanyRepository;
import shop.mtcoding.miniproject2.model.PersonScrap;
import shop.mtcoding.miniproject2.model.PersonScrapRepository;
import shop.mtcoding.miniproject2.model.Post;
import shop.mtcoding.miniproject2.model.PostRepository;
import shop.mtcoding.miniproject2.model.Resume;
import shop.mtcoding.miniproject2.model.ResumeRepository;
import shop.mtcoding.miniproject2.model.Skill;
import shop.mtcoding.miniproject2.model.SkillFilterRepository;
import shop.mtcoding.miniproject2.model.SkillRepository;
import shop.mtcoding.miniproject2.model.User;
import shop.mtcoding.miniproject2.model.UserRepository;

@Transactional // 여기 붙이면 모든 메서드에 다 붙음
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final SkillRepository skillRepository;
    private final SkillFilterRepository skillFilterRepository;
    private final PersonScrapRepository personScrapRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public PostSaveDto 공고등록(PostSaveReqDto postSaveReqDto, int cInfoId) {
        Post post = new Post(postSaveReqDto, cInfoId);
        try {
            postRepository.insert(post);
        } catch (Exception e) {
            throw new CustomApiException("공고 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        System.out.println("테스트" + post.getCreatedAt());
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

        Post realPost = postRepository.findById(post.getId());
        // PostSaveDto postDto = new PostSaveDto(
        // post, new SkillsDto(skill.getId(), skill.getSkills()));
        PostSaveDto postDto = new PostSaveDto(
                realPost, skills);
        return postDto;
    }

    public List<PostTitleRespDto> 기업공고리스트(Integer cInfoId) {
        List<PostTitleRespDto> postTitleList = postRepository.findAllTitleByCInfoId(cInfoId);
        return postTitleList;
    }

    public CompanyPostDetailRespDto 기업공고디테일(int postId, int cInfoId) {

        Post postPS = (Post) postRepository.findById(postId);
        if (postPS == null) {
            throw new CustomApiException("없는 공고 입니다.");
        }
        if (postPS.getCInfoId() != cInfoId) {
            throw new CustomApiException("게시글을 볼 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        // Company companyPS = (Company) companyRepository.findById(cInfoId);
        // Skill skillPS = (Skill) skillRepository.findByPostId(postId);
        // StringTokenizer skills = new StringTokenizer(skillPS.getSkills(), ",");

        CompanyPostDetailRespDto post = postRepository.findByPostInfoIdDetail(postId);
        return post;
    }

    public PostUpdateDto 공고수정하기(PostUpdateReqDto postUpdateReqDto, int postId, int cInfoId) {
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
            e.printStackTrace();
            throw new CustomApiException("공고 수정할 수 없습니다.1", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Skill skillPS = skillRepository.findByPostId(postId);
        String st = postUpdateReqDto.getSkills(); // 수정할 스킬들

        skillPS.setSkills(st);

        int result2 = skillRepository.updateById(skillPS.getId(), skillPS.getPInfoId(), skillPS.getPostId(),
                skillPS.getResumeId(), skillPS.getSkills(), skillPS.getCreatedAt());

        if (result2 != 1) {
            throw new CustomApiException("공고 수정할 수 없습니다.2", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // skill filter
        try {
            skillFilterRepository.deleteByPostId(postPS.getId());
        } catch (Exception e) {
            throw new CustomApiException("공고 수정할 수 없습니다.3", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            String[] skillArr = st.split(",");
            for (int i = 0; i < skillArr.length; i++) {
                skillFilterRepository.insert(skillArr[i], postId, 0);
            }
        } catch (Exception e) {
            throw new CustomApiException("공고 수정할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Post realPost = postRepository.findById(postId);
        PostUpdateDto postDto = new PostUpdateDto(realPost, st);
        return postDto;
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

    public List<PostMainRespDto> 개인공고리스트(Integer pInfoId) {

        List<PostMainRespDto> postList = (List<PostMainRespDto>) postRepository.findAllWithScrapAndCompany(pInfoId);
        for (PostMainRespDto post : postList) {
            if (post.getScrap().getPostId() == null) {
                post.setScrap(null);
            }
        }
        return postList;
    }

    public PersonPostDetailResDto 개인공고디테일(int postId, Integer pInfoId) {

        Post postPS = (Post) postRepository.findById(postId);
        if (postPS == null) {
            throw new CustomApiException("없는 공고 입니다.");
        }

        PersonScrap scrap = personScrapRepository.findByPInfoIdAndPostId(pInfoId, postId);
        Company companyPS = (Company) companyRepository.findById(postPS.getCInfoId());
        Skill skillPS = (Skill) skillRepository.findByPostId(postId);
        User userPS = userRepository.findByCInfoId(companyPS.getId());

        List<Resume> resumeList = (List<Resume>) resumeRepository.findAllByPInfoId(pInfoId);
        List<ResumeDto> resumes = new ArrayList<>();

        for (Resume resume : resumeList) {
            ResumeDto re = new ResumeDto();
            re.setId(resume.getId());
            re.setTitle(resume.getTitle());
            resumes.add(re);
        }
        // PersonPostDetailResDto(Post post, CompanyDto company, ScrapDto scrap,SkillDto
        // skills, List<ResumeDto> resumes)
        PersonPostDetailResDto post = new PersonPostDetailResDto(
                postPS,
                new CompanyDto(companyPS,
                        new UserDto(userPS.getId(), userPS.getEmail())),
                new ScrapDto(scrap),
                new SkillDto(skillPS.getId(), skillPS.getSkills()),
                resumes);

        return post;
    }
}

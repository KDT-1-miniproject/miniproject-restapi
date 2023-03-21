package shop.mtcoding.miniproject2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.model.PersonScrap;
import shop.mtcoding.miniproject2.model.PersonScrapRepository;
import shop.mtcoding.miniproject2.model.Post;
import shop.mtcoding.miniproject2.model.PostRepository;

@Transactional
@Service
public class PersonScrapService {

    @Autowired
    private PersonScrapRepository personScrapRepository;

    @Autowired
    private PostRepository postRepository;

    public void insert(int postId, int pInfoId) {
        Post postPS = postRepository.findById(postId);

        if (postPS == null) {
            throw new CustomApiException("존재하지 않는 공고입니다", HttpStatus.UNAUTHORIZED);
        }

        int result = personScrapRepository.insert(pInfoId, postId);
        if (result != 1) {
            throw new CustomApiException("스크랩 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void delete(int postId, int pInfoId) {
        PersonScrap pScrap = personScrapRepository.findByPInfoIdAndPostId(pInfoId, postId);

        if (pScrap == null) {
            throw new CustomApiException("스크랩 이력이 존재하지 않습니다!");
        }

        if (pScrap.getPInfoId() != pInfoId) {
            throw new CustomApiException("스크랩 취소 권한이 없습니다!");
        }

        int result = personScrapRepository.deleteById(pScrap.getId());
        if (result != 1) {
            throw new CustomApiException("서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

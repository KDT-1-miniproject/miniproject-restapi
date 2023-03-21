package shop.mtcoding.miniproject2.dto.Resume;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

public class ResumeReq {
    @Getter
    @Setter
    public static class ResumeUpdateReqDto {
        private MultipartFile profile;
        private String title;
        private String portfolio;
        private boolean publish;
        private String selfIntro;
        private String name;
        private String phone;
        private String address;
        private String birthday;
        private String skills;
    }
}

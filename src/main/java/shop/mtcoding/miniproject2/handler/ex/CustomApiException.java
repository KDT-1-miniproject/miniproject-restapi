package shop.mtcoding.miniproject2.handler.ex;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {

    private HttpStatus status;

    public CustomApiException(String msg) {
        // super(msg);
        // this.status = HttpStatus.BAD_REQUEST;
        this(msg, HttpStatus.BAD_REQUEST);
        // this는 본인의 생성자를 들고옴
    }

    public CustomApiException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }
}

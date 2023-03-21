package shop.mtcoding.miniproject2.handler.ex;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private HttpStatus status;

    public CustomException(String msg) {
        // super(msg);
        // this.status = HttpStatus.BAD_REQUEST;
        this(msg, HttpStatus.BAD_REQUEST);
        // this는 본인의 생성자를 들고옴
    }

    public CustomException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }

    public CustomException(HttpStatus status) {
        this.status = status;
    }
}

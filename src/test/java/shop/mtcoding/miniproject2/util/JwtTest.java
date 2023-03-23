package shop.mtcoding.miniproject2.util;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtTest {

    @Test
    public void createJwt_test() {
        // given

        // when
        String jwt = JWT
                .create()
                .withSubject("토큰제목") // token 제목
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // token 만료 시간, 예제는 7일
                .withClaim("id", "1") // user의 primary key
                .withClaim("role", "guest") // user의 primary key
                .sign(Algorithm.HMAC512("heesun"));
        // ABC(secret: heeesun) -> 1313AB , ABC(secret: hee) -> 5335KD(token)
        // 이런 식으로 secret (key)을 통해 token을 생성
        // secret은 key이기 때문에 공개되면 xxx
        System.out.println(jwt);
        // then

    }

    @Test
    public void verifyJwt_test() {
        // given

        String jwt = JWT
                .create()
                .withSubject("토큰제목") // token 제목
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // token 만료 시간, 예제는 7일
                .withClaim("id", 1) // user의 primary key
                .withClaim("role", "guest") // user의 primary key
                .sign(Algorithm.HMAC512("heesun"));

        // when
        try {
            DecodedJWT decodeJwt = JWT.require(Algorithm.HMAC512("heesun")).build().verify(jwt);

            int id = decodeJwt.getClaim("id").asInt();
            String role = decodeJwt.getClaim("role").asString();

            System.out.println("id " + id);
            System.out.println("role " + role);

        } catch (SignatureVerificationException e) {
            System.out.println("검증 실패 " + e.getMessage());
        } catch (TokenExpiredException te) {
            System.out.println("토큰 만료 " + te.getMessage());
        }

        // then

    }

}

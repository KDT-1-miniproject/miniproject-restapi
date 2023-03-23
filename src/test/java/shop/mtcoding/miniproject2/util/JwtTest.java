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
        String jwt = JWT
                .create()
                .withSubject("토큰제목")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .withClaim("id", 1) // user의 primary key
                .withClaim("cInfoId", 1)
                .withClaim("pInfoId", 0)
                .withClaim("email", "init@nate.com")
                .sign(Algorithm.HMAC512("jjang"));

        System.out.println(jwt);

    }

    @Test
    public void verifyJwt_test() {

        String jwt = JWT
                .create()
                .withSubject("토큰제목")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .withClaim("id", 1) // user의 primary key
                .withClaim("cInfoId", 1)
                .withClaim("pInfoId", 0)
                .withClaim("email", "init@nate.com")
                .sign(Algorithm.HMAC512("jjang"));

        // when
        try {
            DecodedJWT decodeJwt = JWT.require(Algorithm.HMAC512("jjang")).build().verify(jwt);

            int id = decodeJwt.getClaim("id").asInt();
            int cInfoId = decodeJwt.getClaim("cInfoId").asInt();
            int pInfoId = decodeJwt.getClaim("pInfoId").asInt();
            String email = decodeJwt.getClaim("email").asString();

            System.out.println("id " + id);
            System.out.println("cInfoId " + cInfoId);
            System.out.println("pInfoId " + pInfoId);
            System.out.println("email " + email);

        } catch (SignatureVerificationException e) {
            System.out.println("검증 실패 " + e.getMessage());
        } catch (TokenExpiredException te) {
            System.out.println("토큰 만료 " + te.getMessage());
        }

        // then

    }

}

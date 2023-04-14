package shop.mtcoding.miniproject2.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import shop.mtcoding.miniproject2.model.User;

public class JwtProvider {
    private static final String SUBJECT = "principal";
    private static final int EXP = 1000 * 60 * 60;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    private static final String SECRET = System.getenv("project_secret");

    public static String create(User user) {

        String jwt = JWT
                .create()
                .withSubject(SUBJECT) // token 제목
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP)) // token 만료 시간, 예제는 7일
                .withClaim("id", user.getId()) // user의 primary key
                .withClaim("pInfoId", user.getPInfoId())
                .withClaim("cInfoId", user.getCInfoId())
                .withClaim("email", user.getEmail())
                .sign(Algorithm.HMAC512(SECRET));

        // System.out.println("테스트 : " + jwt);
        return TOKEN_PREFIX + jwt; // 이건 프로토콜
    }

    public static DecodedJWT verify(String jwt) throws SignatureVerificationException, TokenExpiredException {
        // try catch 안 하는 이유 - handler 처리를 못 해서
        DecodedJWT decodeJwt = JWT.require(Algorithm.HMAC512(SECRET)).build().verify(jwt);

        return decodeJwt;
    }

}

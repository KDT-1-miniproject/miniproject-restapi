package shop.mtcoding.miniproject2.config.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import shop.mtcoding.miniproject2.dto.user.UserLoginDto;
import shop.mtcoding.miniproject2.handler.ex.CustomApiException;
import shop.mtcoding.miniproject2.util.JwtProvider;

public class JwtverifyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String prefixJwt = req.getHeader(JwtProvider.HEADER);
        String jwt = prefixJwt.replace(JwtProvider.TOKEN_PREFIX, "");

        try {
            DecodedJWT decodedJwt = JwtProvider.verify(jwt);
            int id = decodedJwt.getClaim("id").asInt();
            int cInfoId = decodedJwt.getClaim("cInfoId").asInt();
            int pInfoId = decodedJwt.getClaim("pInfoId").asInt();
            String email = decodedJwt.getClaim("email").asString();
            HttpSession session = req.getSession();
            UserLoginDto user = UserLoginDto.builder().id(id).cInfoId(cInfoId).pInfoId(pInfoId).email(email).build();

            session.setAttribute("principal", user);

            chain.doFilter(req, resp);

        } catch (SignatureVerificationException e) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        } catch (TokenExpiredException e2) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);

        }
    }

}

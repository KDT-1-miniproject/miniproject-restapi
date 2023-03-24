package shop.mtcoding.miniproject2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import shop.mtcoding.miniproject2.config.interceptor.WebInterceptor;

// public class WebMvcConfig extends WebMvcConfigurerAdapter{
//ISP -> 인터페이스 분리. 하나가 여러가지를 들고 있게 되면 이상하게 사용 될 수 있다...
//근데? 인터페이스는 한 번 만들어지면 수정되면 안됨. 이게 표준이고 규약임.

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebInterceptor())
                .addPathPatterns("/person/*")
                .addPathPatterns("/company/*");
    }

}

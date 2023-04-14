package shop.mtcoding.miniproject2.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import shop.mtcoding.miniproject2.config.filter.JwtverifyFilter;

@Configuration
public class FilterRegisterConfig {

    @Bean
    public FilterRegistrationBean<?> jwtVerifyFilter() {
        FilterRegistrationBean<JwtverifyFilter> registraion = new FilterRegistrationBean<>();
        registraion.setFilter(new JwtverifyFilter());
        registraion.addUrlPatterns("/company/*");
        registraion.addUrlPatterns("/person/*");
        registraion.setOrder(1);
        return registraion;
    }
}

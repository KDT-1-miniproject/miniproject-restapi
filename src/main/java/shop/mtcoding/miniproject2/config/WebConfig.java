package shop.mtcoding.miniproject2.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.web.filter.DelegatingFilterProxy;

public class WebConfig implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration.Dynamic registration = servletContext.addFilter("sessionFilter",
                new DelegatingFilterProxy());
        registration.addMappingForUrlPatterns(null, false, "/*");
    }

}

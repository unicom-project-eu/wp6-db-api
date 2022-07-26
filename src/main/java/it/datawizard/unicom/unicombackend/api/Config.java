package it.datawizard.unicom.unicombackend.api;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public ServletRegistrationBean unicomServletBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new UnicomFHIRServlet(), "/*");
        bean.setLoadOnStartup(1);
        return bean;
    }
}

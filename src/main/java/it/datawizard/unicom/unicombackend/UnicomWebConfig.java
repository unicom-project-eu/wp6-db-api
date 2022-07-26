package it.datawizard.unicom.unicombackend;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnicomWebConfig {
    @Bean
    public ServletRegistrationBean unicomServletBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new UnicomFHIRServlet(), "/*");
        bean.setLoadOnStartup(1);
        return bean;
    }
}

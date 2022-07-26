package it.datawizard.unicom.unicombackend;

import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public ServletRegistrationBean unicomServletBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new UnicomFHIRServlet(), "/*");
        bean.setLoadOnStartup(1);
        return bean;
    }
}

package it.datawizard.unicom.unicombackend;

import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UnicomApplication {

	@Bean
	public ServletRegistrationBean unicomServletBean() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new UnicomFHIRServlet(), "/fhir/*");
		bean.setLoadOnStartup(1);
		return bean;
	}

	public static void main(String[] args) {
		SpringApplication.run(UnicomApplication.class, args);
	}

}
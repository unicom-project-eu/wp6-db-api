package it.datawizard.unicom.unicombackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UnicomBackendApplication {
	@Bean
	public ServletRegistrationBean unicomServletBean() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new RestController(), "/*");
		bean.setLoadOnStartup(1);
		return bean;
	}

	public static void main(String[] args) {
		SpringApplication.run(UnicomBackendApplication.class, args);
	}

}
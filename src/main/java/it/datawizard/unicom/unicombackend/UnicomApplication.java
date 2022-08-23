package it.datawizard.unicom.unicombackend;

import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import it.datawizard.unicom.unicombackend.jpa.repository.SubstanceWithRolePaiRepository;
import kotlin.annotation.Retention;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@SpringBootApplication(scanBasePackages = "it.datawizard.unicom.unicombackend")
public class UnicomApplication implements CommandLineRunner {
	private static Logger LOG = LoggerFactory.getLogger(UnicomApplication.class);

	@Autowired
	UnicomFHIRServlet unicomFHIRServlet;

	@Bean
	public ServletRegistrationBean unicomServletBean() {
		ServletRegistrationBean bean = new ServletRegistrationBean(unicomFHIRServlet, "/fhir/*");
		bean.setLoadOnStartup(1);
		return bean;
	}

	public static void main(String[] args) {
		SpringApplication.run(UnicomApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
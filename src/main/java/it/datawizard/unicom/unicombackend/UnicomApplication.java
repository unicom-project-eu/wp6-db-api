package it.datawizard.unicom.unicombackend;

import ca.uhn.fhir.context.FhirContext;
import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "it.datawizard.unicom.unicombackend")
public class UnicomApplication implements CommandLineRunner {
	private static Logger LOG = LoggerFactory.getLogger(UnicomApplication.class);

	final private UnicomFHIRServlet unicomFHIRServlet;

	@Autowired
	public UnicomApplication(UnicomFHIRServlet unicomFHIRServlet) {
		this.unicomFHIRServlet = unicomFHIRServlet;
	}

	@Bean
	public ServletRegistrationBean<UnicomFHIRServlet> unicomServletBean() {
		ServletRegistrationBean<UnicomFHIRServlet> bean = new ServletRegistrationBean<>(unicomFHIRServlet, "/fhir/*");
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
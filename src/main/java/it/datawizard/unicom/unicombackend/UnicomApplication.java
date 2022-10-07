package it.datawizard.unicom.unicombackend;

import it.datawizard.unicom.unicombackend.dataimport.JsonDataImporter;
import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication(scanBasePackages = "it.datawizard.unicom.unicombackend")
public class UnicomApplication implements CommandLineRunner {
	private static Logger LOG = LoggerFactory.getLogger(UnicomApplication.class);

	private final ConfigurableApplicationContext applicationContext;
	private final UnicomFHIRServlet unicomFHIRServlet;

	@Autowired
	public UnicomApplication(UnicomFHIRServlet unicomFHIRServlet, ConfigurableApplicationContext context) {
		this.unicomFHIRServlet = unicomFHIRServlet;
		this.applicationContext = context;
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
		// argument parsing
		Options options = new Options();

		// data import
		Option importJsonOption = new Option("i", "import", true, "imports the specified json into the DB");
		options.addOption(importJsonOption);

		// do parsing
		CommandLineParser commandLineParser = new DefaultParser();
		HelpFormatter helpFormatter = new HelpFormatter();
		CommandLine commandLine = null;

		try {
			commandLine = commandLineParser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			helpFormatter.printHelp("utility-name", options);

			SpringApplication.exit(applicationContext);
			System.exit(1);
		}

		if (commandLine.hasOption(importJsonOption)) {
			new JsonDataImporter().importData(new File(commandLine.getOptionValue(importJsonOption)));
			System.exit(SpringApplication.exit(applicationContext));
		}
	}
}
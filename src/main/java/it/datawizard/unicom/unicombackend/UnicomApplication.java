package it.datawizard.unicom.unicombackend;

import it.datawizard.unicom.unicombackend.dataimport.DataImportSaveException;
import it.datawizard.unicom.unicombackend.dataimport.JsonDataImporter;
import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
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
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.io.IOException;

@SpringBootApplication(scanBasePackages = {
		"it.datawizard.unicom.unicombackend"
})
public class UnicomApplication implements CommandLineRunner {
	private final static Logger LOG = LoggerFactory.getLogger(UnicomApplication.class);

	private final ConfigurableApplicationContext applicationContext;
	private final DispatcherServlet dispatcherServlet;
	private final UnicomFHIRServlet unicomFHIRServlet;
	private final JsonDataImporter jsonDataImporter;

	@Autowired
	public UnicomApplication(UnicomFHIRServlet unicomFHIRServlet, ConfigurableApplicationContext context, DispatcherServlet dispatcherServlet, JsonDataImporter jsonDataImporter) {
		this.unicomFHIRServlet = unicomFHIRServlet;
		this.applicationContext = context;
		this.dispatcherServlet = dispatcherServlet;
		this.jsonDataImporter = jsonDataImporter;
	}

//	@Bean
//	public DispatcherServlet dispatcherServlet() {
//		return new DispatcherServlet();
//	}

	@Bean
	public ServletRegistrationBean<DispatcherServlet> dispatcherServletServletRegistrationBean() {
		ServletRegistrationBean<DispatcherServlet> bean = new ServletRegistrationBean<>(dispatcherServlet, "/*");
		bean.setLoadOnStartup(1);
		return bean;
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
	public void run(String... args) {
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
			File f = new File(commandLine.getOptionValue(importJsonOption));
			LOG.info("Importing data from " + f.getPath());

			try {
				jsonDataImporter.importData(f);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (DataImportSaveException e) {
				PackagedMedicinalProduct packagedMedicinalProduct = e.getPackagedMedicinalProduct();
				LOG.error("Can't save {}\n\n{}", packagedMedicinalProduct, e);
			}

			System.exit(SpringApplication.exit(applicationContext));
		}
	}
}